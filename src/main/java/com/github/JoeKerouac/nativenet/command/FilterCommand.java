package com.github.JoeKerouac.nativenet.command;

import java.util.ArrayList;
import java.util.List;

import com.joe.command.*;
import com.joe.command.exception.CommandException;
import com.joe.utils.collection.CollectionUtil;

/**
 * 过滤命令
 * 
 * @author JoeKerouac
 * @version 2020年03月24日 20:25
 */
public class FilterCommand implements Command {

    private static final List<String> PKEYS = new ArrayList<>();
    private static final List<String> KEYS = new ArrayList<>();

    static {
        PKEYS.add("add");
        PKEYS.add("rm");
        PKEYS.add("list");
        KEYS.add("ip.src.ip");
        KEYS.add("ip.dest.ip");
        KEYS.add("tcp.src.port");
        KEYS.add("tcp.dest.port");
    }

    @Override
    public String exec(CommandContext input, CommandEngine engine, Environment env) throws CommandException {

        if (CollectionUtil.safeSizeOf(input.params()) != 1) {
            throw new IllegalCommandParamException("参数异常，非法参数：" + input.params());
        }
        Param param = input.params().get(0);

        String paramStr = param.value();

        if (!"--".equals(param.prefix())) {
            throw new IllegalCommandParamException("参数异常，非法参数：" + paramStr);
        }

        String[] pairs = paramStr.split("=");

        if (pairs.length != 2) {
            throw new IllegalCommandParamException("参数异常，非法参数：" + paramStr);
        }

        String[] keys = pairs[0].split("-");

        if (!PKEYS.contains(keys[0]) || !KEYS.contains(keys[1])) {
            throw new IllegalCommandParamException("参数异常，非法参数：" + paramStr);
        }

        env.putIfAbsent(keys[1], new ArrayList<>());
        List<String> datas = env.getEnv(keys[1]);

        String result;

        switch (keys[0]) {
            case "add":
                if (!datas.contains(pairs[1])) {
                    datas.add(pairs[1]);
                }
                result = String.format("%s规则添加成功，添加值：%s\n", keys[1], pairs[1]);
                break;
            case "rm":
                datas.remove(pairs[1]);
                result = String.format("%s规则删除成功，删除值：%s\n", keys[1], pairs[1]);
                break;
            case "list":
                result = String.format("规则%s当前列表值：%s", keys[1], datas);
                break;
            default:
                throw new IllegalCommandParamException("参数异常，非法参数：" + paramStr);
        }

        return result;
    }

    @Override
    public String name() {
        return "filter";
    }

    @Override
    public String help() {
        return "过滤命令用法：\n" + "filter --add-ip.src.ip=192.168.1.1       : 该命令将会过滤源ip是192.168.1.1的数据包\n"
            + "filter --add-ip.dest.ip=192.168.1.1      : 该命令将会过滤目标ip是192.168.1.1的数据包\n"
            + "filter --add-tcp.src.port=8080           : 该命令将会过滤源端口是8080的数据包\n"
            + "filter --add-tcp.dest.port=8080          : 该命令将会过滤目标端口是8080的数据包\n"
            + "filter --rm-ip.src.ip=192.168.1.1        : 该命令将移除对源ip是192.168.1.1的数据包的过滤\n"
            + "filter --rm-ip.dest.ip=192.168.1.1       : 该命令将移除对目标ip是192.168.1.1的数据包的过滤\n"
            + "filter --rm-ip.src.port=8080             : 该命令将移除对源端口是8080的数据包的过滤\n"
            + "filter --rm-ip.dest.port=8080            : 该命令将移除对目标端口是8080的数据包的过滤\n"
            + "filter --list-ip.src.ip                  : 该命令将列举当前对源ip的过滤列表\n"
            + "filter --list-ip.dest.ip                 : 该命令将列举当前对目标ip的过滤列表\n"
            + "filter --list-ip.src.port                : 该命令将列举当前对源端口的过滤列表\n"
            + "filter --list-ip.dest.port               : 该命令将列举当前对目标端口的过滤列表\n";
    }
}
