package org.cloudbus.cloudsim.examples;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.power.PowerDatacenter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class Benchmark_Simulation {
    private static List<Vm> vmList;
    private static List<Cloudlet> cloudletList;
    public static void main(String[] args) {
        Log.printLine("Starting Benchmark simulation...");
        try {
            // Initialize the CloudSim library
            Calendar calendar = Calendar.getInstance();
            CloudSim.init(Constants.NUM_USER, calendar, Constants.TRACE_EVENTS);
            // Create Data centers
            PowerDatacenter datacenter =  Helper.createDatacenter(Constants.DATA_CENTER_NAME, Constants.BENCHMARK, 1.0);
            datacenter.setDisableMigrations(true);
            // Create Broker
            DatacenterBroker broker = Helper.createBroker();
            int brokerId = broker.getId();
            // Create VM
            vmList = Helper.createVms(brokerId);
            broker.submitVmList(vmList);
            // Create Cloudlet
            cloudletList = Helper.createCloudletList(brokerId);
            broker.submitCloudletList(cloudletList);
            // Start Simulation
            double lastClock = CloudSim.startSimulation();
            // Get recieved cloudlets
            List<Cloudlet> newList = broker.getCloudletReceivedList();
            Log.printLine("Received " + newList.size() + " cloudlets");
            // Stop Simulation
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

        Log.printLine("Not Power Aware example finished!");
    }

}
