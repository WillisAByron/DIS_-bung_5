import java.util.Random;

public class Client extends Thread{

	private PersistenceManager pM;
	private int id;
	
	public Client(PersistenceManager pM, int id){
		this.pM = pM;
        this.id = id;
	}

	@Override
	public void run() {
        // Number of Transactions
        int transactions = new Random().nextInt(10);
		for (int i = 0; i <= transactions; i++) {
            int transactionID = this.pM.beginTransaction();
            String data = toString();

            // Number of Writes
            int writes = new Random().nextInt(10);
            for (int j = 0; j <= writes; j++){
                Long pageID = Long.valueOf((id * 10 + new Random().nextInt(10)));

                pM.write(transactionID, pageID, data);
                sleepRandom();
            }
            System.out.println("Client " + id + " wrote " + writes + " Pages.");
            pM.commit(transactionID);
            sleepRandom();
		}
        System.out.println("Client " + id + " committed " + transactions + " Transactions.");
	}

    private void sleepRandom(){
        try {
            Thread.sleep(new Random().nextInt(1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
            return;
        }
    }

    @Override
    public String toString(){
        return "Client: " + id;
    }
}