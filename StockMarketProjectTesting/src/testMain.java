import java.sql.SQLException;

public class testMain {

	public static void main(String[] args) throws SQLException {
			buyAndHold a = new buyAndHold();
			a.buyAndHoldEx();
			
			strategy200 b = new strategy200();
			b.strategy200Ex();
			
			strategy200percent3 c = new strategy200percent3();
			c.strategy200percent3Ex();
	}

}
