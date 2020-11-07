package fr.istic.csr.tp2;

import fr.istic.csr.tp2.Client;

class Site {

/* Constantes associees au site */

static final int STOCK_INIT = 5;
static final int STOCK_MAX = 10;
static final int BORNE_SUP = 8;
static final int BORNE_INF = 2;
private int stockActuelle = 0;
private int numero = 0;
 //nombre de clients ayant dejà restitué leur velo au moment de l'actuelle visite du camion
 private int nbRestitutionActuelle = 0;
 //nombre de clients ayant dejà restitué leur velo au moment de la visite precedente du camion
 private int nbRestitutionPrecedent = 0;


    public Site(int number){
        numero = number ;
        stockActuelle = STOCK_INIT ;
    }


    public synchronized int getStockActuelle() {
        return stockActuelle;
    }

    public int getNumero() {
        return numero;
    }

    //Cette methode va servir au camion pour connaitre le nombre de lient ayanty été servis et pouvoir arrêter de tourner
    public synchronized int getNbNouvelRestitution() {
         int n = nbRestitutionActuelle - nbRestitutionPrecedent ;
            nbRestitutionPrecedent = nbRestitutionActuelle ;
        return n ;
    }

    private synchronized void setNbRestitutionActuelle() {
        nbRestitutionActuelle ++ ;
    }


    public synchronized void destocker(int nombreVelo,String nomClient, Camion camion ) {
        while ( stockActuelle-nombreVelo < 0 ){
            //System.out.println("Le client "+nomClient+" est en attente sur site S"+numero+" !!  pour emprunter un velo. Aucun velo disponible sur le site actuellement !!");
            try {  this.wait(); } catch (InterruptedException e) { e.printStackTrace(); }
        }
        int ancienStock = this.getStockActuelle() ;
        stockActuelle -= nombreVelo ;
        if (camion !=null){
            int nouvelleChargeCamion = camion.getCharge()+nombreVelo ;
            System.out.println("Le camion a visité  le site S"+this.getNumero()+". Ancien stock: "+ancienStock+". Nouveau stock:"+this.getStockActuelle()+". STOCK-INIT:"+this.STOCK_INIT+". STOCK-MAX: "+this.STOCK_MAX+". BORNE-INF: "+this.BORNE_INF+". BORNE-SUP: "+this.BORNE_SUP+".");
            System.out.println("Info du camion: ancienne charge =>"+camion.getCharge() +" ,  nouvelle charge => "+nouvelleChargeCamion+" ");
        }else { System.out.println("Le client "+nomClient+" emprunte sur le site S"+this.getNumero()+". AncienStock: "+ancienStock+".  Le stock actuel du site : "+this.getStockActuelle());
        }

        this.notifyAll();
    }

    public synchronized void stocker(int nombreVelo,String nomClient, Camion camion) {
        while ( stockActuelle+nombreVelo > STOCK_MAX ){
            try {  this.wait(); } catch (InterruptedException e) { e.printStackTrace(); }
        }
        int ancienStock = this.getStockActuelle() ;
        stockActuelle += nombreVelo ;

        if (camion != null){
            int nouvelleChargeCamion = camion.getCharge()-nombreVelo ;
            System.out.println("Le camion a visité  le site S"+this.getNumero()+". Ancien stock: "+ancienStock+". Nouveau stock:"+this.getStockActuelle()+". STOCK-INIT:"+this.STOCK_INIT+". STOCK-MAX: "+this.STOCK_MAX+". BORNE-INF: "+this.BORNE_INF+". BORNE-SUP: "+this.BORNE_SUP+".");
            System.out.println("Info du camion: ancienne charge =>"+camion.getCharge() +" ,  nouvelle charge => "+nouvelleChargeCamion+" ");
        }else {
            //Après une restition de velo, on incremente donc le nombre de velo restitués.
            setNbRestitutionActuelle() ;
           System.out.println("Le client "+nomClient+" restitue sur le site S"+this.getNumero()+". AncienStock: "+ancienStock+".  Le stock actuel du site : "+this.getStockActuelle());
        }
        this.notifyAll();
    }

}
