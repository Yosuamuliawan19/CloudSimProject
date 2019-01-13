package org.cloudbus.cloudsim.examples;

import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.power.PowerDatacenter;
import org.cloudbus.cloudsim.power.PowerHost;
import org.cloudbus.cloudsim.power.PowerPe;
import org.cloudbus.cloudsim.power.PowerVmAllocationPolicySingleThreshold;
import org.cloudbus.cloudsim.power.models.PowerModelLinear;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

import java.util.*;

public class Single_Threshold_Simulation {
    private static List<Vm> vmList;
    private static List<Cloudlet> cloudletList;
    private static double utilizationThreshold = 0.85;
    public static void main(String[] args) {
        Log.printLine("Starting simulation for DVFS data center...");
        try{
            // initializing cloudsim libary
            Calendar calendar = Calendar.getInstance();
            CloudSim.init(Constants.NUM_USER, calendar, Constants.TRACE_EVENTS);
            PowerDatacenter datacenter = Helper.createDatacenter(Constants.DATA_CENTER_NAME, Constants.DVFS, utilizationThreshold);
            // create data center broker
            DatacenterBroker broker = Helper.createBroker();
            int brokerId = broker.getId();
            // create virtual machines
            vmList = Helper.createVms(brokerId);
            broker.submitVmList(vmList);
            // create cloudlet
            cloudletList = Helper.createCloudletList(brokerId);
            broker.submitCloudletList(cloudletList);
            // Start simulation
            double lastClock = CloudSim.startSimulation();
            // Recieved cloudlet
            List<Cloudlet> newList = broker.getCloudletReceivedList();
            Log.printLine("Received " + newList.size() + " cloudlets");
            // Stop simulation
            CloudSim.stopSimulation();

            Helper.printCloudletList(newList);

            int totalTotalRequested = 0;
            int totalTotalAllocated = 0;
            ArrayList<Double> sla = new ArrayList<Double>();
            int numberOfAllocations = 0;
            for (Map.Entry<String, List<List<Double>>> entry : datacenter.getUnderAllocatedMips().entrySet()) {
                List<List<Double>> underAllocatedMips = entry.getValue();
                double totalRequested = 0;
                double totalAllocated = 0;
                for (List<Double> mips : underAllocatedMips) {
                    if (mips.get(0) != 0) {
                        numberOfAllocations++;
                        totalRequested += mips.get(0);
                        totalAllocated += mips.get(1);
                        double _sla = (mips.get(0) - mips.get(1)) / mips.get(0) * 100;
                        if (_sla > 0) {
                            sla.add(_sla);
                        }
                    }
                }
                totalTotalRequested += totalRequested;
                totalTotalAllocated += totalAllocated;
            }

            double averageSla = 0;
            if (sla.size() > 0) {
                double totalSla = 0;
                for (Double _sla : sla) {
                    totalSla += _sla;
                }
                averageSla = totalSla / sla.size();
            }

            Log.printLine();
            Log.printLine(String.format("Total simulation time: %.2f sec", lastClock));
            Log.printLine(String.format("Energy consumption: %.2f kWh", datacenter.getPower() / (3600 * 1000)));
            Log.printLine(String.format("Number of VM migrations: %d", datacenter.getMigrationCount()));
            Log.printLine(String.format("Number of SLA violations: %d", sla.size()));
            Log.printLine(String.format("SLA violation percentage: %.2f%%", (double) sla.size() * 100 / numberOfAllocations));
            Log.printLine(String.format("Average SLA violation: %.2f%%", averageSla));
            Log.printLine();


        } catch (Exception e) {
            e.printStackTrace();
            Log.printLine("Unwanted errors happen");
        }
        Log.printLine("DVFS Simulation finished!");
    }
}
