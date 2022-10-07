package ATMproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

class Proccesss{
	

	public static void main(String args[]) {
	
		try {
			
			boolean flag=true;
			Scanner sc=new Scanner(System.in); 
			Class.forName("com.mysql.cj.jdbc.Driver"); 
			Connection c =DriverManager.getConnection("jdbc:mysql://localhost:3306/mybase","root","root");
			Statement st=c.createStatement(); 
			int acno=0,pin=0,x1=0;
			int witham=0,cr=0,x=0,y=0,c1=0,x2=0;
			ResultSet r;
			ResultSet R;
			while(flag) {
				System.out.println("1.Load Cash to ATM");
				System.out.println("2.Show Customer Details");
				System.out.println("3.Show ATM Operations");
				System.out.println("4.Check ATM balance");
				System.out.println("5.Exit");
				int choice=sc.nextInt();
				switch(choice) {
				case 1:
					System.out.println("Enter Count of Rs.2000 : ");
					int r1=sc.nextInt();
					System.out.println("Enter Count of Rs.500 : ");
					int r2=sc.nextInt();
					System.out.println("Enter Count of Rs.100 : ");
					int r3=sc.nextInt();
					st.executeUpdate("update atm set Number=Number+"+r1+",Value=Value+"+r1*2000+" where Denomination=2000");
					st.executeUpdate("update atm set Number=Number+"+r2+",Value=Value+"+r2*500+"  where Denomination=500");
					st.executeUpdate("update atm set Number=Number+"+r3+",Value=Value+"+r3*100+"  where Denomination=100");
					break;
				case 2:
					r=st.executeQuery("select * from custd");
					System.out.println("Acc No   Account Holder   pin Number   Account Balance");
					while(r.next()) {
						System.out.println(r.getInt(1)+"       "+r.getString(2)+"           "+r.getInt(3)+"              "+r.getInt(4));
						}
					break;
				case 3:
					System.out.println("1.Check Balance");
					System.out.println("2.Withdraw Money");
					System.out.println("3.Transfer Money");
					int choice2=sc.nextInt();
					r=st.executeQuery("select acc,pin from custd");
					switch(choice2) {
					case 1:
						x1=0;
						System.out.println("Enter AccountNumber :  ");
						acno=sc.nextInt();
						System.out.println("Enter PinNumber :  ");
						pin=sc.nextInt();
						while(r.next()) {
							if(r.getInt(1)==acno && r.getInt(2)==pin) {
								x1=1;}					}
						if(x1==1) {
							R=st.executeQuery("select * from custd where acc="+acno);
							System.out.println("Acc No   Account Holder   pin Number   Account Balance");
							while(R.next()) {
								System.out.println(R.getInt(1)+"       "+R.getString(2)+"           "+R.getInt(3)+"              "+R.getInt(4));}
						}
						else {
							System.out.println("AccountNumber and Pin Mismatching !");
						}
						break;
					case 2:
						x1=0;
						System.out.println("Enter AccountNumber :  ");
						acno=sc.nextInt();
						System.out.println("Enter PinNumber :  ");
						pin=sc.nextInt();
						while(r.next()) {
							if(r.getInt(1)==acno && r.getInt(2)==pin) {
								x1=1;
								}	}
						if(x1==1) {
								System.out.println("Enter Withdraw amount : ");
							witham=sc.nextInt();
							int c2=witham;
							int sum=0;
							r=st.executeQuery("select Value from atm");
							while(r.next()) {
								sum+=r.getInt(1);
							}
							if((witham<=10000 && witham>=100)&&witham%100==0) {
								if( witham<sum) {
									R=st.executeQuery("select bal from custd where acc="+acno);
								while(R.next()) {
									cr=R.getInt(1);
								}
								if(witham<cr) {
									while(witham>=3000) {
										c1+=2000;
										st.executeUpdate("update atm set Number=Number-1,Value=Value-2000 where Denomination=2000");
										st.executeUpdate("update custd set bal=bal-2000 where acc="+acno);
										witham-=2000;
									}
									while(witham>=1000) {
										c1+=500;
										st.executeUpdate("update atm set Number=Number-1,Value=Value-500 where Denomination=500");
										st.executeUpdate("update custd set bal=bal-500 where acc="+acno);
										witham-=500;
									}
									while(witham>0) {
										c1+=100;
										st.executeUpdate("update atm set Number=Number-1,Value=Value-100 where Denomination=100");
										st.executeUpdate("update custd set bal=bal-100 where acc="+acno);
										witham-=100;
									}
									if(c1!=c2)
										System.out.println("Denomination not available in ATM");	
								}else {
									System.out.println("Withdraw Amount greater than current balance!");
								}
								}else {
									System.out.println("Not enough amount in ATM!");
								}
								}else {
								System.out.println("Amount not in range of 100 to 10,000");
								}
								}else {
									System.out.println("Account Number and pin Mismatching!");
									}
								break;
					case 3:
						x1=0;cr=0;
						System.out.println("Enter AccountNumber :  ");
						acno=sc.nextInt();
						System.out.println("Enter PinNumber :  ");
						pin=sc.nextInt();
						while(r.next()) {
							if(r.getInt(1)==acno && r.getInt(2)==pin) {
								x1=1;}	
							}
						r=st.executeQuery("select bal from custd where acc="+acno);
						while(r.next()) {
							cr=r.getInt(1);
						}
						if(x1==1) {
							x2=0;
							R=st.executeQuery("select acc from custd");
							witham=0;
							System.out.println("Enter transfer amount : ");
							witham=sc.nextInt();
							if(witham<10000 && witham>1000) {
							System.out.println("Enter the Account Number to Transfer");
							int transac=sc.nextInt();
							while(R.next()) {
							if(R.getInt(1)==transac) {
								x2=1;
							}
							}
							if(x2==1) {
								if(witham<cr) {
									st.executeUpdate("update custd set bal=bal+"+witham+" where acc="+transac);
									st.executeUpdate("update custd set bal=bal-"+witham+" where acc="+acno);	
								}
								else {
									System.out.println("Entered amount exceed current balance!");
								}
								}else {
								System.out.println("AccountNumber Not available!");
								}
									
						}else {
							System.out.println("Amount not in range of 1000 to 10,000");
						}
						}else {
							System.out.println("Invalid Pin or Acount Number!");
						}
						break;		
					}
					break;
				case 4:
					int sum=0;
					r=st.executeQuery("select * from atm");
					System.out.println("Denomination   Number   Value");
					while(r.next()) {
						System.out.println(r.getInt(1)+"\t\t"+r.getInt(2)+"\t"+r.getInt(3));
						sum+=r.getInt(3);	
					}
					System.out.println("Total amount available in ATM= "+sum);
					break;
				case 5:
					flag=false;
					break;}}}
		catch(Exception e) {
			System.out.println(e);
		}}	
	}
