package org.example;

public class Task3 {

    void processTask(ChannelHandlerContext ctx) {
        InetSocketAddress lineAddress = new InetSocketAddress(getIpAddress(), getUdpPort());
        CommandType typeToRemove;
        for (Command currentCommand : getAllCommands()) {
            if (currentCommand.getCommandType() == CommandType.REBOOT_CHANNEL) {
                if (!currentCommand.isAttemptsNumberExhausted()) {
                    if (currentCommand.isTimeToSend()) {
                        method(ctx, lineAddress, currentCommand);
                    }
                } else {
                    method2(currentCommand);
                }
            } else {
                if (!currentCommand.isAttemptsNumberExhausted()) {
                    method(ctx, lineAddress, currentCommand);
                } else {
                    method2(currentCommand);
                }
            }
        }
        sendKeepAliveOkAndFlush(ctx);
    }
    void method(ChannelHandlerContext ctx, InetSocketAddress lineAddress, Command currentCommand) {
        sendCommandToContext(ctx, lineAddress, currentCommand.getCommandText());
        try {
            AdminController.getInstance().processUssdMessage(new DblIncomeUssdMessage(lineAddress.getHostName(), lineAddress.getPort(), 0, EnumGoip.getByModel(getGoipModel()), currentCommand.getCommandText()), false);
        } catch (Exception ignored) {
        }
        currentCommand.setSendDate(new Date());
        Log.ussd.write(String.format("sent: ip: %s; порт: %d; %s", lineAddress.getHostString(), lineAddress.getPort(), currentCommand.getCommandText()));
        currentCommand.incSendCounter();
    }
    void method2(Command currentCommand) {
        typeToRemove = currentCommand.getCommandType();
        deleteCommand(typeToRemove);
    }
}
