package fr.istic.csr.tp2;

public class Camion extends Thread {
    private Site[] tousLesSite; // L'ensemble des site à visiter
    private int charge = 30 ; //Le nombre de velot trasportés par le camion
    private int nbClients = 0 ; //Le nombre de client du système
    private int nbClientServis = 0 ; //Le nombre de client ayant restitué leur velo


    public Camion (Site[]sites,int nbClient ){
        tousLesSite = sites ;
        nbClients = nbClient ;
    }

    public int getCharge(){ return charge ; }


    /*Permet d'équilibrer le stock d'un site
    le synchronized pour ne pas que leSiteCourant.stockActuelle ne soit modifié par quelqu'un d'autre pendant qu'on le manipule
     */
    private synchronized void visiter(Site leSiteCourant) {
        //Compter le nombre global de clients servis
        nbClientServis +=  leSiteCourant.getNbNouvelRestitution();

        if(leSiteCourant.getStockActuelle() < leSiteCourant.BORNE_INF ){
            int aStcker = leSiteCourant.STOCK_INIT - leSiteCourant.getStockActuelle() ;
            if( charge >= aStcker ){
                //Remettre le stock au niveau initial
                leSiteCourant.stocker(aStcker,"",this);
                charge -= aStcker ;
            }else {
                //Sinon ramener le stock au niveau niveau le plus proche possible du stock initial
                leSiteCourant.stocker(charge,"",this);
                charge = 0 ;
            }

        }else if(leSiteCourant.getStockActuelle() > leSiteCourant.BORNE_SUP ){
            int aStcker = leSiteCourant.getStockActuelle() - leSiteCourant.STOCK_INIT ;
                leSiteCourant.destocker(aStcker,"",this);
                charge += aStcker ;
        }
        //Avec cette methode on est sûr ne pas pouvoir depasser le  STOCK_MAX
    }

    //Permet d'équilibrer le stock de chaque site
    public void run() {
        int taille = tousLesSite.length;
        int compteur = 0 ;
        int dureeDuDeplacement = 0 ; //
        while (compteur< taille) {
            visiter(tousLesSite[compteur]);

            if (compteur==taille-1){
                /*Ceci assure la boucle infinie
                on met la valeur de compteur à 0 pour revenir parcours parcours tousLesSite, entrainant ainsi une boucle infinie.
                */
                compteur = 0 ;

                //Depuis le dernier site si le camion retourne au premier site alors le chemin parcouru est la taille du tableau
                dureeDuDeplacement =  taille*100 ;
            }else{
                dureeDuDeplacement = (tousLesSite[compteur+1].getNumero()-tousLesSite[compteur].getNumero())*100 ;
                compteur++;
            }
            //Le camion ne fait rien pendant le deplacement
            try { Thread.sleep(dureeDuDeplacement); } catch(InterruptedException e) {}

            if (nbClientServis == nbClients){
                compteur=taille ;
                System.out.println("\n\n Tous les clients ont été servis. Bravo !! \n\n");
            }
        }
    }





}
