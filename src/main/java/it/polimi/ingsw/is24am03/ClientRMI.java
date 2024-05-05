package it.polimi.ingsw.is24am03;

import it.polimi.ingsw.is24am03.server.model.game.RemoteGameController;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientRMI implements Client{
    Registry registry;
    private String username;
    private RemoteGameController gameController;
    private final CliView view;
    //private Game gameModel;
    private final String ip;
    private final int port;

    private boolean connectionClosed;

    public ClientRMI(String hostName, int portNumber, CliView view) {
        boolean connected = false;
        this.connectionClosed = false;
        RemoteGameController temp = null;
        this.ip = hostName;
        this.port = portNumber;
        this.view = view;
        while(!connected){
            try{
                this.registry= LocateRegistry.getRegistry(hostName, portNumber);
                String remoteObjectName = "game_controller";
                temp =  (RemoteGameController) registry.lookup(remoteObjectName);

                connected = true;
            }catch(RemoteException e){
            }catch(NotBoundException e){
            }

        }

        this.gameController = temp;
    }


    public void CreateGame(int nPlayers, String nickname){

        try {
            this.gameController.createGame(nPlayers, nickname);
            System.out.println("Game created successfully");
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid arguments");
        } catch (RuntimeException e) {
            System.out.println("Game already created");
        } catch (RemoteException e){

        }


    }

}