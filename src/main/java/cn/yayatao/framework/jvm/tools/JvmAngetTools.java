package cn.yayatao.framework.jvm.tools;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.lang.management.ManagementFactory;
import java.net.URL;
import java.nio.file.Paths;

import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;

/********
 * jvm anget 类, 加入本jar包开源 在运行时拿到 jvm anget 的核心 接口  Instrumentation
 *  实现  动态的 anget require sun tools jar
 * @author fireflyhoo
 *
 */
public class JvmAngetTools {

	private static Instrumentation instrumentation;

	public static JvmAngetTools getInstance() {
		return Hander.instance;
	}

	private JvmAngetTools() {
		init();
	}

	public static class Hander {
		private static JvmAngetTools instance = new JvmAngetTools();
	}

	public void init() {
		try {
			String name = ManagementFactory.getRuntimeMXBean().getName();
			String pid = name.split("@")[0];
			VirtualMachine vm = VirtualMachine.attach(pid);
			URL jarFilePath = JvmAngetTools.class.getProtectionDomain().getCodeSource().getLocation();
			vm.loadAgent(Paths.get(jarFilePath.toURI()).toFile().getAbsolutePath(), null);
			vm.detach();
		} catch (AttachNotSupportedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (AgentLoadException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 这个方法必须写，在agent调用时会被启用
	 */
	public static void premain(String agentArgs, Instrumentation instrumentation) {
		JvmAngetTools.instrumentation = instrumentation;
	}

	public static void agentmain(String agentArgs, Instrumentation instrumentation) {
		JvmAngetTools.instrumentation = instrumentation;
	}

	/***
	 * get Jvm Instrumentation instance
	 * 
	 * @return
	 */
	public Instrumentation getInstrumentation() {
		return JvmAngetTools.instrumentation;
	}

	/****
	 * Returns an implementation-specific approximation of the amount of storage
	 * consumed by the specified object. The result may include some or all of
	 * the object's overhead, and thus is useful for comparison within an
	 * implementation but not between implementations. The estimate may change
	 * during a single invocation of the JVM.
	 * 
	 * @param o
	 * @return
	 */
	public long sizeOf(Object o) {
		if (instrumentation == null) {
			throw new IllegalStateException("Can not access instrumentation environment.\n"
					+ "Please check if jar file containing SizeOfAgent class is \n"
					+ "specified in the java's \"-javaagent\" command line argument.");
		}
		return instrumentation.getObjectSize(o);
	}
}
