package com.joizhang.naiverpc.netty.proxy;

import com.itranswarp.compiler.JavaStringCompiler;
import com.joizhang.naiverpc.proxy.ServiceStub;
import com.joizhang.naiverpc.proxy.StubFactory;
import com.joizhang.naiverpc.remoting.Transport;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DynamicStubFactory implements StubFactory {

    private final static String STUB_SOURCE_TEMPLATE =
            "package com.joizhang.naiverpc.netty.client.stubs;\n" +
                    "import com.joizhang.naiverpc.netty.serialize.SerializeSupport;\n" +
                    "\n" +
                    "public class %s extends AbstractStub implements %s {\n" +
                    "    @Override\n" +
                    "    public %s %s(%s) {\n" +
                    "        return SerializeSupport.parse(\n" +
                    "                invokeRemote(\n" +
                    "                        new RpcRequest(\n" +
                    "                                \"%s\",\n" +
                    "                                \"%s\",\n" +
                    "                                SerializeSupport.serialize(%s)\n" +
                    "                        )\n" +
                    "                )\n" +
                    "        );\n" +
                    "    }\n" +
                    "}";

    @Override
    @SuppressWarnings("unchecked")
    public <T> T createStub(Transport transport, Class<T> serviceClass) {
        try {
            // 填充模板
            String stubSimpleName = serviceClass.getSimpleName() + "Stub";
            String classFullName = serviceClass.getName();
            String stubFullName = "com.joizhang.naiverpc.netty.client.stubs." + stubSimpleName;
            // TODO 暂时只支持单一方法，单一参数
            Method method = serviceClass.getMethods()[0];
            String returnName = method.getReturnType().getName();
            String methodName = method.getName();
            String[] methodArgs = constructMethodArgs(method);
            String source = String.format(STUB_SOURCE_TEMPLATE, stubSimpleName, classFullName,
                    returnName, methodName, methodArgs[0], classFullName, methodName, methodArgs[1]);
            // 编译源代码
            JavaStringCompiler compiler = new JavaStringCompiler();
            Map<String, byte[]> results = compiler.compile(stubSimpleName + ".java", source);
            // 加载编译好的类
            Class<?> clazz = compiler.loadClass(stubFullName, results);

            // 把Transport赋值给桩
            ServiceStub stubInstance = (ServiceStub) clazz.getDeclaredConstructor().newInstance();
            stubInstance.setTransport(transport);
            // 返回这个桩
            return (T) stubInstance;
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    private String[] constructMethodArgs(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        assert parameterTypes.length == 1;
        List<String> classNames = Arrays.stream(parameterTypes)
                .map(Class::getName).collect(Collectors.toList());
        StringBuilder args = new StringBuilder();
        StringBuilder vars = new StringBuilder();
        for (int i = 0; i < classNames.size(); i++) {
            if (i != classNames.size() - 1) {
                args.append(classNames.get(i)).append(" arg").append(i).append(", ");
                vars.append("arg").append(i).append(", ");
            } else {
                args.append(classNames.get(i)).append(" arg").append(i);
                vars.append("arg").append(i);
            }
        }
        return new String[]{args.toString(), vars.toString()};
    }

}
