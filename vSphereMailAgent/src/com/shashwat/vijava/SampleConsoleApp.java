package com.shashwat.vijava;

import java.net.URL;

import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.ServiceInstance;

public class SampleConsoleApp {
	public SampleConsoleApp(String vCenterServerAddress, String userName, String password) {
		ServiceInstance vcInstance = getServiceInstance(vCenterServerAddress, userName, password);
		getAllVMNames(vcInstance);
	}

	private ServiceInstance getServiceInstance(String vCenterServerAddress, String userName, String password) {
		try {
			String urlString = "https://" + vCenterServerAddress + "/sdk";
			return new ServiceInstance(new URL(urlString), userName, password, true);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	private void getAllVMNames(ServiceInstance vcInstance) {
		getManagedEntitiesNames(vcInstance, "VirtualMachine");
	}

	private String getManagedEntitiesNames(ServiceInstance vcInstance, String managedEntityTypeP) {
		StringBuilder vmList = new StringBuilder();
		vmList.append("VM names : ").append("\n");
		try {
			if (vcInstance != null) {
				Folder rootFolder = vcInstance.getRootFolder();
				ManagedEntity[] mes = new InventoryNavigator(rootFolder).searchManagedEntities(managedEntityTypeP);
				if (mes == null || mes.length == 0) {
					vcInstance.getServerConnection().logout();
					return "Unable to find the vm names";
				} else {
					for (ManagedEntity entity : mes) {
						vmList.append(entity.getName()).append("\n");
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return vmList.toString();
	}

	public static void main(String[] args) {
		new SampleConsoleApp("<vCENTER-SERVER-IP-ADDRESS>", "<USERNAME>", "<PASSWORD>");
	}
}
