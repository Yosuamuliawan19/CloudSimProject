package org.cloudbus.cloudsim.examples;

import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.power.*;
import org.cloudbus.cloudsim.power.models.PowerModelLinear;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Helper {
    public static PowerDatacenter createDatacenter(String name, String type, double utilizationThreshold) throws Exception {
        // To store machines
        List<PowerHost> hostList = new ArrayList<PowerHost>();
        for (int i = 0; i < Constants.HOSTS_NUMBER; i++) {
            // CPU Cores in each host
            List<PowerPe> peList = new ArrayList<PowerPe>();
            peList.add(new PowerPe(0, new PeProvisionerSimple(Constants.HOSTS_MIP), new PowerModelLinear(Constants.HOSTS_MAX_POWER, Constants.HOSTS_STATIC_POWER_PERCENTAGE))); // need to store PowerPe id and MIPS Rating
            // Adding host inside hostlist
            hostList.add(
                    // Adds a new powerhost
                    new PowerHost(
                            i,
                            new RamProvisionerSimple(Constants.HOSTS_RAM),
                            new BwProvisionerSimple(Constants.HOSTS_BANDWITH),
                            Constants.HOSTS_STORAGE,
                            peList,
                            new VmSchedulerTimeShared(peList)
                    )
            );
        }
        DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
                Constants.HOSTS_ARCHITECTURE, Constants.HOSTS_OS, Constants.HOSTS_VMM, hostList, Constants.HOSTS_TIME_ZONE, Constants.HOSTS_COST, Constants.HOSTS_COST_PER_MEM, Constants.HOSTS_COST_PER_STORAGE, Constants.HOSTS_COST_PER_BANDWITH);

        PowerDatacenter powerDatacenter = null;
        try {
            if (type == Constants.BENCHMARK){
                powerDatacenter = new PowerDatacenterNonPowerAware(
                        name,
                        characteristics,
                        new PowerVmAllocationPolicySingleThreshold(hostList, utilizationThreshold),
                        new LinkedList<Storage>(),
                        5.0);
            }else if (type == Constants.DVFS){
                powerDatacenter = new PowerDatacenter(
                        name,
                        characteristics,
                        new PowerVmAllocationPolicySingleThreshold(hostList, utilizationThreshold),
                        new LinkedList<Storage>(),
                        5.0);
            }else{
                powerDatacenter = new PowerDatacenter(
                        name,
                        characteristics,
                        new PowerVmAllocationPolicySingleThreshold(hostList, utilizationThreshold),
                        new LinkedList<Storage>(),
                        5.0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return powerDatacenter;
    }
    public static DatacenterBroker createBroker() {
        DatacenterBroker broker = null;
        try {
            broker = new DatacenterBroker(Constants.BROKER_NAME);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return broker;
    }
    public static List<Vm> createVms(int brokerId) {
        List<Vm> vms = new ArrayList<Vm>();
        for (int i = 0; i < Constants.VM_NUMBER; i++) {
            vms.add(
                    new Vm(i, brokerId, Constants.VM_MIPS, Constants.VM_PE_NUMBER, Constants.VM_RAM, Constants.VM_BANDWITH, Constants.VM_SIZE, Constants.VM_VMM, new CloudletSchedulerDynamicWorkload(Constants.VM_MIPS, Constants.VM_PE_NUMBER))
            );
        }
        return vms;
    }
    public static List<Cloudlet> createCloudletList(int brokerId) {
        List<Cloudlet> list = new ArrayList<Cloudlet>();
        for (int i = 0; i < Constants.CLOUDLET_NUMBER; i++) {
            Cloudlet cloudlet = new Cloudlet(i, Constants.CLOUDLET_LENGTH, Constants.CLOUDLET_PE, Constants.CLOUDLET_FILE_SIZE, Constants.CLOUDLET_OUTPUT_SIZE, new UtilizationModelStochastic(), new UtilizationModelStochastic(), new UtilizationModelStochastic());
            cloudlet.setUserId(brokerId);
            cloudlet.setVmId(i);
            list.add(cloudlet);
        }
        return list;
    }
    public static void printCloudletList(List<Cloudlet> list) {
        int size = list.size();
        Cloudlet cloudlet;

        String indent = "\t";
        Log.printLine();
        Log.printLine("========== OUTPUT ==========");
        Log.printLine("Cloudlet ID" + indent + "STATUS" + indent
                + "Resource ID" + indent + "VM ID" + indent + "Time" + indent
                + "Start Time" + indent + "Finish Time");

        DecimalFormat dft = new DecimalFormat("###.##");
        for (int i = 0; i < size; i++) {
            cloudlet = list.get(i);
            Log.print(indent + cloudlet.getCloudletId());

            if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS) {
                Log.printLine(indent + "SUCCESS"
                        + indent + indent + cloudlet.getResourceId()
                        + indent + cloudlet.getVmId()
                        + indent + dft.format(cloudlet.getActualCPUTime())
                        + indent + dft.format(cloudlet.getExecStartTime())
                        + indent + indent + dft.format(cloudlet.getFinishTime())
                );
            }
        }
    }
}
