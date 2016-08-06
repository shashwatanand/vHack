package com.shashwat.vijava;

import java.net.URL;
import java.util.Scanner;

import com.vmware.vim25.Description;
import com.vmware.vim25.VirtualDeviceConfigSpec;
import com.vmware.vim25.VirtualDeviceConfigSpecOperation;
import com.vmware.vim25.VirtualEthernetCard;
import com.vmware.vim25.VirtualEthernetCardNetworkBackingInfo;
import com.vmware.vim25.VirtualMachineConfigSpec;
import com.vmware.vim25.VirtualMachineFileInfo;
import com.vmware.vim25.VirtualPCNet32;
import com.vmware.vim25.mo.Datacenter;
import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ResourcePool;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.Task;

public class CreateVM {
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		String address, userName, password, vmName, operation;
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter vCenter or ESXi IP address ?");
		address = scanner.nextLine();
		
		System.out.println("Enter username of " + address + " ?");
		userName = scanner.nextLine();
		
		System.out.println("Enter password of " + address + " ?");
		password = scanner.nextLine();
		
		System.out.println("Enter name of virtual machine name "
				+ "you want to create?");
		vmName = scanner.nextLine();
		
		System.out.println("Enter operation - reboot|poweron|poweroff" 
				+ "|reset|standby|suspend|shutdown");
		operation = scanner.nextLine();
		scanner.close();
		
		
		String dcName = "datacenter";
		long memorySizeInMB = 500;
		int noOfCPU = 1;
		String guestOsId = "sles10Guest";
		long diskSizeInKB = 1000000;
		// mode: persistent|independent_persistent,
		// independent_nonpersistent
		String diskMode = "persistent";
		String dataStoreName = "storage1 (2)";
		String netName = "VM Network";
		String nicName = "Network Adapter 1";
		// type: generated|manual|assigned by VC
		String nicAddressType = "generated";
		
		ServiceInstance serviceInstance = new ServiceInstance(new URL(address), 
				userName, password, true);
		
		Folder rootFolder = serviceInstance.getRootFolder();
		Datacenter datacenter = (Datacenter) new InventoryNavigator(rootFolder)
				.searchManagedEntity("Datacenter", dcName);
		ResourcePool resourcePool = (ResourcePool) new InventoryNavigator(rootFolder)
				.searchManagedEntities("ResourcePool")[0];
		Folder vmFolder = datacenter.getVmFolder();
		
		// VM config spec
		VirtualMachineConfigSpec vmSpec = new VirtualMachineConfigSpec();
		vmSpec.setName(vmName);
		vmSpec.setAnnotation("VirtualMachine Annotation");
		vmSpec.setNumCPUs(noOfCPU);
		vmSpec.setMemoryMB(memorySizeInMB);
		vmSpec.setGuestId(guestOsId);
		
		// VM virtual devices
		int key = 100;
		VirtualDeviceConfigSpec scsiSpec = createScsiSpec(key);
		VirtualDeviceConfigSpec diskSpec = createDiskSpec(key, dataStoreName,
				diskSizeInKB, diskMode);
		VirtualDeviceConfigSpec nicSpec = createNicSpec(netName, nicName, nicAddressType);
		
		vmSpec.setDeviceChange(new VirtualDeviceConfigSpec[] {scsiSpec, 
				diskSpec, nicSpec});
		
		// cCreate vmx file
		VirtualMachineFileInfo fileInfo = new VirtualMachineFileInfo();
		fileInfo.setVmPathName("[" + dataStoreName + "]");
		vmSpec.setFiles(fileInfo);
		
		// Create VM
		Task task = vmFolder.createVM_Task(vmSpec, resourcePool, null);
		if (task.waitForMe() == Task.SUCCESS) {
			System.out.println("VM with name " + vmName + " created sucessfully");
		} else {
			System.out.println("Unable to create VM");
		}
	}

	private static VirtualDeviceConfigSpec createNicSpec(String netName, String nicName, String nicAddressType) throws Exception {
		VirtualDeviceConfigSpec nicSpec = new VirtualDeviceConfigSpec();
		nicSpec.setOperation(VirtualDeviceConfigSpecOperation.add);
		
		VirtualEthernetCard nic = new VirtualPCNet32();
		VirtualEthernetCardNetworkBackingInfo backingInfo = new VirtualEthernetCardNetworkBackingInfo();
		backingInfo.setDeviceName(netName);
		
		Description deviceInfo = new Description();
		deviceInfo.setLabel(nicName);
		deviceInfo.setSummary(netName);
		nic.setDeviceInfo(deviceInfo);
		nic.setAddressType(nicAddressType);
		nic.setBacking(backingInfo);
		nic.setKey(0);
		
		nicSpec.setDevice(nic);
		return nicSpec;
	}

	private static VirtualDeviceConfigSpec createDiskSpec(int key, String dataStoreName, long diskSizeInKB,
			String diskMode) {
		// TODO Auto-generated method stub
		return null;
	}

	private static VirtualDeviceConfigSpec createScsiSpec(int key) {
		// TODO Auto-generated method stub
		return null;
	}
}
