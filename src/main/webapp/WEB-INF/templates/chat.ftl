<!doctype html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <script type="text/javascript" src="/webjars/jquery/jquery.min.js"></script>
    <link rel="stylesheet" type="text/css" href="/webjars/bootstrap/css/bootstrap.min.css">
    <script type="text/javascript" src="/webjars/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="/webjars/sockjs-client/sockjs.min.js"></script>
    <script type="text/javascript" src="/webjars/stomp-websocket/stomp.min.js"></script>
    <script>
        let stompClient = null;

        function setConnected(connected) {
            $("#connect").prop("disabled", connected);
            $("#disconnect").prop("disabled", !connected);
            if (connected) {
                $("#conversation").show();
            } else {
                $("#conversation").hide();
            }

            $("#messages").html("");
        }

        function connect() {
            console.log("Trying to connect");
            let socket = new SockJS("/message-websocket");
            stompClient = Stomp.over(socket);
            stompClient.connect({}, function (frame) {
                console.log("Connected: " + frame);
                setConnected(true);
                stompClient.subscribe("/topic/message/" + "${pageId}", function (message) {
                    let mess = JSON.parse(message.body);
                    showMessage(mess.sender + ': ' + mess.text);
                })
            })
        }

        function disconnect() {
            if (stompClient != null) {
                stompClient.disconnect();
            }
            setConnected(false);
            console.log("Disconnected")
        }

        function sendMessage() {
            stompClient.send("/app/message", {}, JSON.stringify({'text': $("#message").val(), 'sender': $("#name").val(), 'pageId': "${pageId}"}));
        }

        function showMessage(message) {
            $("#messages").append("<tr><td>" + message + "</td></tr>")
        }

        $(function () {
            $("form").on('submit', function (e) {
                e.preventDefault();
            });
            $("#connect").click(function () {
                connect();
            });
            $("#disconnect").click(function () {
                disconnect();
            });

            $("#send").click(function () {
                sendMessage();
            });
        })
    </script>
</head>

<body>
<div class="container" id="main-content">
    <div class="row">
        <div class="col-md-6">
            <form class="inline">
                <div class="form-group">
                    <label>
                        WebSocket connection:
                    </label>

                    <button id="connect" type="button" class="btn btn-default">
                        Connect
                    </button>
                    <button id="disconnect" type="button" class="btn btn-default" disabled="disabled">
                        Disconnect
                    </button>
                </div>
            </form>
        </div>

        <div class="col-md-6">
            <form class="inline">
                <div class="form-group">
                    <label for="name">
                        What is your name?
                    </label>
                    <input type="text" id="name" placeholder="your name here..." class="form-control">

                    <label for="message">
                        Text your message
                    </label>
                    <input type="text" id="message" placeholder="message..." class="form-control">

                    <button id="send" class="btn btn-default" type="button">Send message</button>
                </div>
            </form>
        </div>
    </div>

    <div class="row">
        <div class="col-md-12">
            <table id="conversation" class="table table-striped">
                <thread>
                    <tr>
                        <th>Messages</th>
                    </tr>
                </thread>

                <tbody id="messages">


                </tbody>
            </table>
        </div>
    </div>
</div>
</body>
