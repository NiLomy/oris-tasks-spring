<#assign sf=JspTaglibs["http://www.springframework.org/security/tags"]>

<@sf.authorize access="!isAuthenticated()">
    Login
</@sf.authorize>
<@sf.authorize access="isAuthenticated()">
    Logout
</@sf.authorize>

<@sf.authorize access="hasRole('ADMIN')">
    Manage users
</@sf.authorize>

<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>

<body>
<div>
    <p>
        This is an <strong> example </strong> paragraph.
    </p>
</div>
</body>
</html>
