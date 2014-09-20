package org.pdpblr.vc;

public class VCCommand {
	private final String command;
	private VCCommand(String commandP) {
		this.command = commandP;
	}
	public static final VCCommand USERNAME = new VCCommand("USERNAME");
	public static final VCCommand PASSWORD = new VCCommand("PASSWORD");
	public static final VCCommand GETVMNAMES = new VCCommand("GETVMNAMES");
	
	public String toString() {
		return this.command;
	}
	
	public static final String commandHelp = "\n=====================================================================================\n" 
			+ "VCIPADDRESS=<IPADDRESS>;USERNAME=<username>;PASSWORD=<password>;COMMAND=GETVMNAMES \n"
			+ "VCIPADDRESS=<IPADDRESS>;USERNAME=<username>;PASSWORD=<password>;COMMAND=GETDATACENTERNAMES \n"
			+ "VCIPADDRESS=<IPADDRESS>;USERNAME=<username>;PASSWORD=<password>;COMMAND=GETHOSTS\n"
			+ "VCIPADDRESS=<IPADDRESS>;USERNAME=<username>;PASSWORD=<password>;COMMAND=GETRESOURCEPOOL\n"
			+ "VCIPADDRESS=<IPADDRESS>;USERNAME=<username>;PASSWORD=<password>;COMMAND=POWERON_VM;VMNNAME=Dummy-Test\n"
			+ "VCIPADDRESS=<IPADDRESS>;USERNAME=<username>;PASSWORD=<password>;COMMAND=POWEROFF_VM;VMNNAME=Dummy-Test\n"
			+ "VCIPADDRESS=<IPADDRESS>;USERNAME=<username>;PASSWORD=<password>;COMMAND=SUSPEND_VM;VMNNAME=Dummy-Test\n"
			+ "VCIPADDRESS=<IPADDRESS>;USERNAME=<username>;PASSWORD=<password>;COMMAND=REBOOT_VM;VMNNAME=Dummy-Test\n"
			+ "VCIPADDRESS=<IPADDRESS>;USERNAME=<username>;PASSWORD=<password>;COMMAND=RESUME_VM;VMNNAME=Dummy-Test\n";
}
