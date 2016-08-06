package com.shashwat.vijava;

import java.net.URL;
import java.util.Scanner;

import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.Task;
import com.vmware.vim25.mo.VirtualMachine;

public class VMPowerOps {
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
		
		System.out.println("Enter name of virtual machine, on which "
				+ "you want to do operation?");
		vmName = scanner.nextLine();
		
		System.out.println("Enter operation - reboot|poweron|poweroff" 
				+ "|reset|standby|suspend|shutdown");
		operation = scanner.nextLine();
		scanner.close();
		
		ServiceInstance serviceInstance = new ServiceInstance(new URL(address), 
				userName, password, true);
		
		Folder rootFolder = serviceInstance.getRootFolder();
		VirtualMachine vm = (VirtualMachine)new InventoryNavigator(rootFolder).
				searchManagedEntity("VirtualMachine", vmName);
		if (vm == null) {
			System.out.println("No VM with name " + vmName + " found");
			serviceInstance.getServerConnection().logout();
			return;
		}
		Task task;
		switch (operation) {
			case "reboot":
				vm.rebootGuest();
				System.out.println(vmName + " guest OS rebooted");
				break;
			case "poweron":
				task = vm.powerOnVM_Task(null);
				if (task.waitForMe() == Task.SUCCESS) {
					System.out.println(vmName + " powered on");
				}
				break;
			case "poweroff":
				task = vm.powerOffVM_Task();
				if (task.waitForMe() == Task.SUCCESS) {
					System.out.println(vmName + " powered off");
				}
				break;
			case "reset":
				task = vm.resetVM_Task();
				if (task.waitForMe() == Task.SUCCESS) {
					System.out.println(vmName + " reset");
				}
				break;
			case "standby":
				vm.standbyGuest();
				System.out.println(vmName + " guest OS standby");
				break;
			case "suspend":
				task = vm.suspendVM_Task();
				if (task.waitForMe() == Task.SUCCESS) {
					System.out.println(vmName + " suspended");
				}
				break;
			case "shutdown":
				vm.shutdownGuest();
				System.out.println(vmName + " guest OS shutdown");
				break;

			default:
				System.out.println("Wrong operation");
				break;
		}
	}
}
