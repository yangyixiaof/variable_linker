package cn.yyx.research.program.ir.storage.connection;

import java.util.Iterator;
import java.util.Set;

public class ConnectHelper {
	
	public static boolean ConnectedNodesShouldBeInCluster(IIRConnection conn) {
		return !ConnectedNodesShouldNotBeInCluster(conn);
	}
	
	public static boolean ConnectedNodesShouldNotBeInCluster(IIRConnection conn) {
		Set<Connect> conns = conn.GetAllConnects();
		Iterator<Connect> citr = conns.iterator();
		while (citr.hasNext()) {
			Connect cnct = citr.next();
			Class<? extends Connect> type = cnct.getClass();
			if (SuperConnect.class.isAssignableFrom(type) || MethodJumpConnect.class.isAssignableFrom(type) || VariableConnect.class.isAssignableFrom(type)) {
				return true;
			}
		}
		return false;
	}
	
}
