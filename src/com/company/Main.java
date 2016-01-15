package com.company;

import com.sun.org.apache.xalan.internal.utils.XMLSecurityManager;

import java.util.Random;

public class Main {

    public static void main(String[] args) {
        SimulateAirport(1000,2,0.1);
    }

    private static int getPoissonRandom(double mean){
        Random r = new Random();
        double L = Math.exp(-mean);
        int k = 0;
        double p = 1.0;
        do{
            p = p * r.nextDouble();
            k++;
        }
        while(p > L);
        return k - 1;
    }

    private static void printReady(Plane plane, boolean landing){
        System.out.print("Fly " + plane.Id + " er klar for ");
        if(landing){
            System.out.println("landing.");
            return;
        }
        System.out.println("avgang.");
    }
    private static void printAction(Plane plane, boolean landing, int currentIteration){
        System.out.print("Fly " + plane.Id + " har ");
        if(landing){
            System.out.print("landet.");
        }
        else{
            System.out.print("tatt av.");
        }
        System.out.println("   Ventetid: " + (currentIteration - plane.BirthTime));
    }



    public static void SimulateAirport(int tics, double meanArriving, double meanDeparting){
        System.out.println("Velkommen til Halden lufthavn");
        System.out.println("Simuleringen vil kjøre " + tics + " ganger.");
        System.out.println("Det forventes " + meanArriving + " ankomster per tidsenhet og " + meanDeparting + " avganger.");
        LimitedQueue arrivalQueue = new LimitedQueue(10);
        LimitedQueue departureQueue = new LimitedQueue(10);
        //resetting planeIdCounter
        Plane.nextAvailableId = 0;
        int AntallFlyBehandlet = 0;
        int FlyLandet = 0;
        int FlyTattAv = 0;
        int FlyAvvist = 0;
        int TotalVentLanding = 0;
        int TotalVentAvgang = 0;

        for(int i = 0; i < tics; i++){
            System.out.println("Runde " + (i+1));
            //Adding new planes to queue
            for(int j = 0; j < getPoissonRandom(meanArriving); j++){
                Plane newPlane = new Plane(i);
                if(arrivalQueue.EnQueue(newPlane)){
                    printReady(newPlane,true);
                }
                else{
                    System.out.println("Landingskø full. Fly " + newPlane.Id + " henvist til Rygge.");
                    FlyAvvist++;
                }

            }
            for(int j = 0; j < getPoissonRandom(meanDeparting); j++){
                Plane newPlane = new Plane(i);
                if(departureQueue.EnQueue(newPlane)){
                    printReady(newPlane,false);
                }
                else{
                    System.out.println("Avgangskø full. Passasjerer på fly " + newPlane.Id + " henvist til buss for fly");
                    FlyAvvist++;
                }
            }

            if(arrivalQueue.IsEmpty()){
                if(departureQueue.IsEmpty()){
                    System.out.println("Flyplassen er tom");
                    continue;
                }
                Plane toDepart = (Plane)departureQueue.DeQueue();
                TotalVentAvgang += (i-toDepart.BirthTime);
                FlyTattAv++;
                printAction(toDepart,false,i);
                continue;
            }
            Plane toArrive = (Plane)arrivalQueue.DeQueue();
            TotalVentLanding += (i-toArrive.BirthTime);
            FlyLandet++;
            printAction(toArrive,true,i);
        }
        //Printing stats
        System.out.println("***STÆTZ***");
        System.out.println("Simuleringen ferdig etter " + tics + " tidsenheter");
        int totaltBehandlet = Plane.nextAvailableId - FlyAvvist - arrivalQueue.Count() - departureQueue.Count();
        System.out.println("Totalt antall fly behandlet: " + totaltBehandlet);
        System.out.println("Antall fly landet: " + FlyLandet);
        System.out.println("Antall fly tatt av: " + FlyTattAv);
        System.out.println("Fly avvist: " + FlyAvvist);
        System.out.println("Fly i landingskø: " + arrivalQueue.Count());
        System.out.println("Fly i avgangskø:" + departureQueue.Count());
        int ledigeTidsenheter = tics - totaltBehandlet;
        System.out.println("Prosent ledig tid: " + (double)(tics/100)*ledigeTidsenheter);
        if(TotalVentLanding != 0){
            System.out.println("Snitt ventetid landing: " + (double)(TotalVentLanding/FlyLandet));
        }
        else{
            System.out.println("Snitt ventetid landing: 0");
        }
        if(TotalVentLanding != 0){
            System.out.println("Snitt ventetid avgang: " + (double)(TotalVentAvgang/FlyTattAv));
        }
        else{
            System.out.println("Snitt ventetid avgang: 0");
        }


    }
}
