package com.github.JoeKerouac.nativenet.command;

import com.joe.command.exception.CommandException;

/**
 * 命令参数错误
 *
 * @author JoeKerouac
 * @version 2020年03月24日 21:01
 */
public class IllegalCommandParamException extends CommandException {

    public IllegalCommandParamException(String message) {
        super(message);
    }

}
