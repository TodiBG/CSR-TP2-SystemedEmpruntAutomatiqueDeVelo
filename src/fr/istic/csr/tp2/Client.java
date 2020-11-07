package fr.istic.csr.tp2;


public class Client extends Thread {
    Site  siteDepart ;
    Site  siteArrivee ;
    String nomClient ;
    public Client(int Id ,Site depart, Site arrivee ){
        nomClient = "C"+Id;
        siteDepart = depart ;
        siteArrivee = arrivee ;
    }

    public void emprunterVelo (){
        siteDepart.destocker(1,nomClient,null);
    }

    public void resituerVelo (){
        siteArrivee.stocker(1,nomClient,null);
    }

    public void run(){
        int dureeDuDeplacement = (siteArrivee.getNumero()-siteDepart.getNumero())*100 ;

        if( dureeDuDeplacement < 0 ) { dureeDuDeplacement = -dureeDuDeplacement ; } // dureeDeDeplacement doit être positif

        emprunterVelo();
        //Le client ne fait rien pendant le deplacement
        try { Thread.sleep(dureeDuDeplacement); } catch(InterruptedException e) {}
        //Le client restitue le velo après le deplacement
        resituerVelo() ;
    }





}
