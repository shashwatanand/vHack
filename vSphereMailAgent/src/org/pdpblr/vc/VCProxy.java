package org.pdpblr.vc;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;

import org.pdpblr.util.Util;

import com.vmware.vim25.HostVMotionCompatibility;
import com.vmware.vim25.TaskInfo;
import com.vmware.vim25.VirtualMachineMovePriority;
import com.vmware.vim25.VirtualMachinePowerState;
import com.vmware.vim25.mo.ComputeResource;
import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.Task;
import com.vmware.vim25.mo.VirtualMachine;

public class VCProxy {
	private Hashtable<String, String> table;
	
	private List<String> attachments;
	
	public VCProxy(Hashtable<String, String> tableP) {
		table = tableP;
	}
	
	public String getVcIPAddress() {
		return table.get("VCIPADDRESS");
	}

	public String getUserName() {
		return table.get("USERNAME");
	}

	public String getPassword() {
		return table.get("PASSWORD");
	}

	public String getCommand() {
		return table.get("COMMAND");
	}
	
	// TODO Y do u need to login to vc Server every time?
	private ServiceInstance getServiceInstance() {
		try {
			String urlString = "https://" + getVcIPAddress() + "/sdk";
			return new ServiceInstance(new URL(urlString), getUserName(), getPassword(), true);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	public String processCommand() {
		try {
			VCCommandCode command = VCCommandCode.getCommand("VC_CMD_"+ getCommand());
			switch(command) {
				case VC_CMD_GETVMNAMES :
					return getManagedEntitiesNames("VirtualMachine") + VCCommand.commandHelp;
				case VC_CMD_GETHOSTS :
					return getManagedEntitiesNames("HostSystem") + VCCommand.commandHelp;
				case VC_CMD_GETDATACENTERNAMES :
					return getManagedEntitiesNames("Datacenter") + VCCommand.commandHelp;
				case VC_CMD_GETRESOURCEPOOL :
					return getManagedEntitiesNames("ResourcePool") + VCCommand.commandHelp;
				case VC_CMD_POWERON_VM :
					return vmPowerOps(table.get("VMNNAME"), "poweron") + VCCommand.commandHelp;
				case VC_CMD_POWEROFF_VM :
					return vmPowerOps(table.get("VMNNAME"), "poweroff") + VCCommand.commandHelp;
				case VC_CMD_RESET_VM :
					return vmPowerOps(table.get("VMNNAME"), "reset") + VCCommand.commandHelp;
				case VC_CMD_SUSPEND_VM :
					return vmPowerOps(table.get("VMNNAME"), "suspend") + VCCommand.commandHelp;
				case VC_CMD_REBOOT_VM :
					return vmPowerOps(table.get("VMNNAME"), "reboot") + VCCommand.commandHelp;
				case VC_CMD_RESUME_VM :
					return vmPowerOps(table.get("VMNNAME"), "poweron") + VCCommand.commandHelp;
				case VC_CMD_GETLOGFILE :
					return getLogFile() + VCCommand.commandHelp;
				case VC_CMD_HELP :
					return VCCommand.commandHelp;
				case VC_CMD_HOSTVMOTION :
					return hostVmotion(table.get("VMNNAME"), table.get("NEWHOSTNAME")) + VCCommand.commandHelp;
				case VC_CMD_STORAGEVMOTION :
					return storageVmotion() + VCCommand.commandHelp;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	private String storageVmotion() {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("deprecation")
	private String hostVmotion(String sVMname, String newHostName) {
		StringBuilder output = new StringBuilder();
		try {
			ServiceInstance vcInstance = getServiceInstance();
			if (vcInstance != null) {
				Folder rootFolder = vcInstance.getRootFolder();
				VirtualMachine vm = (VirtualMachine) new InventoryNavigator(rootFolder).searchManagedEntity("VirtualMachine", sVMname);
				HostSystem newHost = (HostSystem) new InventoryNavigator(rootFolder).searchManagedEntity("HostSystem", newHostName);
				
				ComputeResource cr = (ComputeResource) newHost.getParent();
				String[] checks = {"cpu", "software"};
				HostVMotionCompatibility[] vmcs = vcInstance.queryVMotionCompatibility(vm, new HostSystem[] {newHost}, checks);
				String[] comps = vmcs[0].getCompatibility();
				if (checks.length != comps.length) {
					output.append("CPU/software NOT compatible. Exiting....\n");
					return output.toString();
				}
				Task task = vm.migrateVM_Task(cr.getResourcePool(), newHost, VirtualMachineMovePriority.highPriority, VirtualMachinePowerState.poweredOn);
				if (task.waitForMe() == Task.SUCCESS) {
					output.append("vMotioned vmname : ").append(sVMname).append(" to host : ").append(newHostName).append("\n");
				} else {
					output.append("vMotioned failed \n");
					TaskInfo info = task.getTaskInfo();
					output.append(info.getError().getFault()).append("\n");
				}
			}
		} catch (Exception ex) {
			output.append("Exception thrown : " + ex.getMessage());
			ex.printStackTrace();
		}
		return output.toString();
	}
	
	@SuppressWarnings("deprecation")
	private String vmPowerOps(String sVMname, String operation) {
		StringBuilder output = new StringBuilder();
		try {
			ServiceInstance vcInstance = getServiceInstance();
			if (vcInstance != null) {
				Folder rootFolder = vcInstance.getRootFolder();
				VirtualMachine vm =  (VirtualMachine) new InventoryNavigator(rootFolder).searchManagedEntity("VirtualMachine", sVMname);
				if (vm == null) {
					output.append("No VM  "+ sVMname + " found");
					return output.toString();
				}
				if ("poweron".equalsIgnoreCase(operation)) {
					Task task = vm.powerOnVM_Task(null);
					if (task.waitForMe() == Task.SUCCESS) {
						output.append(sVMname + " powered on");
					}
				} else if ("poweroff".equalsIgnoreCase(operation)) {
					Task task = vm.powerOffVM_Task();
					if (task.waitForMe() == Task.SUCCESS) {
						output.append(sVMname + " powered off");
					}
				} else if ("reset".equalsIgnoreCase(operation)) {
					Task task = vm.resetVM_Task();
					if (task.waitForMe() == Task.SUCCESS) {
						output.append(sVMname + " reseted");
					}
				} else if ("suspend".equalsIgnoreCase(operation)) {
					Task task = vm.suspendVM_Task();
					if (task.waitForMe() == Task.SUCCESS) {
						output.append(sVMname + " suspended");
					}
				} else if ("reboot".equalsIgnoreCase(operation)) {
					vm.rebootGuest();
				} else {
					output.append("Invalid operation. Exiting...");
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return output.toString();
	}
	
	private String getManagedEntitiesNames(String managedEntityTypeP) {
		StringBuilder vmList = new StringBuilder();
		vmList.append("Command requested : ").append(getCommand()).append("\n");
		try {
			ServiceInstance vcInstance = getServiceInstance();
			if (vcInstance != null) {
				Folder rootFolder = vcInstance.getRootFolder();
				ManagedEntity[] mes =  new InventoryNavigator(rootFolder).searchManagedEntities(managedEntityTypeP);
				if (mes == null || mes.length == 0) {
					vcInstance.getServerConnection().logout();
					return "Unable to process the command";
				} else {
					for (ManagedEntity entity : mes) {
						vmList.append(entity.getName()).append("\n");
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (Util.isStringEmpty(vmList.toString())) {
			return vmList.toString();
		} else {
			return "Output : NULL";
		}
	}
	
	private String getLogFile()
	{
		//String logDir = "C$/Documents and Settings/All Users/Application Data/VMware/VMware VirtualCenter/Logs";
		String logDir = "C$/Documents and Settings/All Users/Application Data/VMware/VMware VirtualCenter/Logs";
		String logFile = "vimtool.log";
        NtlmPasswordAuthentication auth1 = new NtlmPasswordAuthentication( "vmware", getUserName(), getPassword() );

        SmbFile f1;
		try {
			f1 = new SmbFile( "smb://" + getVcIPAddress() + "/"  + logDir + "/" + logFile, auth1 );
	        System.out.println("Exists=" + f1.exists());
	        System.out.println("getDiskFreeSpace=" + f1.getDiskFreeSpace());
	        
	        NtlmPasswordAuthentication auth2 = new NtlmPasswordAuthentication( "", "rchowdhury", "" );

	        SmbFile f2 = new SmbFile( "smb://127.0.0.1/c$/aaa/" + logFile , auth2);
	        System.out.println("Exists=" + f2.exists());
	        
	        f1.copyTo(f2);
	        
	        System.out.println("Exists=" + f2.exists());
	        attachments = new ArrayList<String>();
	        attachments.add("C:/aaa/" + logFile);
	        
	        return "Successfully transferred file";
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (SmbException e) {
			e.printStackTrace();
		}
		return "Failure";
	}
	
	public List<String> getAttachments() {
		return attachments;
	}
}
