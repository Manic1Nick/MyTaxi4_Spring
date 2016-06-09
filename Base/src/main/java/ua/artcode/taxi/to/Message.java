package ua.artcode.taxi.to;

public class Message {

    private String methodName;

    private MessageBody messageBody;

    public Message() {
    }

    public Message(String methodName, MessageBody messageBody) {
        this.methodName = methodName;
        this.messageBody = messageBody;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public MessageBody getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(MessageBody messageBody) {
        this.messageBody = messageBody;
    }
}