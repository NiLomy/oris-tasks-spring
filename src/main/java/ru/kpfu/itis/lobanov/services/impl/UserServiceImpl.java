package ru.kpfu.itis.lobanov.services.impl;

import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kpfu.itis.lobanov.configs.MailConfig;
import ru.kpfu.itis.lobanov.dtos.CreateUserRequestDto;
import ru.kpfu.itis.lobanov.dtos.UserDto;
import ru.kpfu.itis.lobanov.model.User;
import ru.kpfu.itis.lobanov.repositories.UserRepository;
import ru.kpfu.itis.lobanov.services.UserService;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final MailConfig mailConfig;
    private final JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserDto> findAllByName(String name) {
        return userRepository.findAllByName(name)
                .stream().map(user -> new UserDto(user.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public UserDto createUser(CreateUserRequestDto dto, String url) {
        String code = RandomString.make(128);
        User user = User.builder()
                .name(dto.getName())
                .username(dto.getEmail())
                .verificationCode(code)
                .password(passwordEncoder.encode(dto.getPassword()))
                .build();
        sendVerificationCode(dto.getEmail(), user.getName(), code, url);
        User u = userRepository.save(user);
        return new UserDto(u.getName());
    }

    @Override
    public boolean verify(String code) {
        Optional<User> user = userRepository.findByVerificationCode(code);
        if (user.isPresent()) {
            User u = user.get();
            u.setVerificationCode(null);
            u.setEnabled(true);
            userRepository.save(u);
            return true;
        }
        return false;
    }

    @Override
    public void sendVerificationCode(String mail, String name, String code, String baseUrl) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        String content = mailConfig.getContent();

        try {
            helper.setFrom(mailConfig.getFrom(), mailConfig.getSender());
            helper.setTo(mail);
            helper.setSubject(mailConfig.getSubject());
            content = content.replace("{name}", name);
            content = content.replace("{url}", baseUrl + "/users/verification?code=" + code);
            helper.setText(content);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
