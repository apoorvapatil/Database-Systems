package dbms_assignment3;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.sql.*;
import java.util.*;
public class populate{
    

	static int count=0;
	
	public static Connection con = null;
	public static Statement st  = null;
	public static PreparedStatement ps = null;
	public static PreparedStatement ps1 = null;
	
    public static void main(String[] args)
    {
        JSONParser parser = new JSONParser();
        BufferedReader br = null;
        	
        // Insert into YELP_USER
        System.out.println("Inserting data into YELP_USER");
        PopulateYelpUser(br,parser, args[2]);
    	System.out.println("User Entries added");
       
    	// Insert into BUSINESS
    	System.out.println("Inserting data into BUSINESS");
    	PopulateBusiness(br,parser, args[3]);
    	System.out.println("Business entries added");
        
    	// Insert into BUSINESS_CATEGORY
    	System.out.println("Inserting data into BUSINESS_CATEGORY");
        PopulateBusinessCategory(br,parser, args[3]);
        System.out.println("Business Categories entries added");

        // Insert into BUSINESSATTRIBUTES
        System.out.println("Inserting data into BUSINESSATTRIBUTES");
        PopulateBusinessAttributes(br,parser, args[3]);
        System.out.println("Attributes Entries added");
        
        // Insert into REVIEWS
        System.out.println("Inserting data into REVIEWS");
    	PopulateReviews(br,parser,args[0]);
    	System.out.println("Review data entries added");
    	
    	// Insert into CHECKIN
    	/*System.out.println("Inserting data into CHECKIN");
    	PopulateCheckin(br,parser, args[1]);
        System.out.println("Checkin Entries added");*/
        
        // Close connection after populating data
    	jdbc_close();
    }
    
    public static void jdbc_query(String str)
    {
    	// Connect to Oracle 11g

        try{  
	        // Load the driver class
			if(count==0){
				
			    Class.forName("oracle.jdbc.driver.OracleDriver");  
	              
	            // Create connection object  
				con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521/orcl","hr","oracle");
				count++;
			    st =con.createStatement();
			}
			
	        // Execute query
	        st.executeQuery(str);
	        
	    } catch(Exception e)
		{ 
			System.out.println(e);
			System.exit(0);
		} 
	}

    // Close connection
    public static void jdbc_close()
    {
    	try {
			con.close();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
    }

    // Populate REVIEWS table
    public static void PopulateReviews(BufferedReader br, JSONParser parser, String input)
    {
    	
    	// Delete any previous data in reviews table
    	String str="DELETE FROM REVIEWS";

    	jdbc_query(str);  
    	
    	// File content reader
    	String line,str1,text,text2;
    	try {
			br = new BufferedReader(new FileReader(input));
		} catch (FileNotFoundException e1) {
			
			e1.printStackTrace();
		}
    	
    	
    	try {
    		int count=0,flag=0;
    		
    		// For every line in the file
    		while((line = br.readLine()) != null) {

    			Object obj = parser.parse(line);
				JSONObject jsonObject = (JSONObject) obj;
				JSONObject votes =  (JSONObject)jsonObject.get("votes");
				
				
				// Get objects
				long funny = (long) votes.get("funny");
				long cool = (long) votes.get("cool");
				long useful = (long)votes.get("useful");
				long total_votes = funny + cool + useful;
				String user_id=(String) jsonObject.get("user_id");
                String review_id = (String) jsonObject.get("review_id");
                long stars = (long) jsonObject.get("stars");
                text=(String)jsonObject.get("text");
                String type=(String) jsonObject.get("type");
                String business_id=(String) jsonObject.get("business_id");
                String date_review = (String) jsonObject.get("date");
                
				text2 = text.substring(text.length()/2, text.length());
                text = text.substring(0, text.length()/2);
                
                
                str1="INSERT INTO REVIEWS VALUES (?,?,?,?,?,?,?,?,?)";
                if(count == 0) {
                    ps = con.prepareStatement(str1);
                }
                flag=1;
                ps.setLong(1, total_votes);
                ps.setString(2, user_id.toString());
                ps.setString(3, review_id);
                ps.setLong(4, stars);
                ps.setString(5, type.toString());
                
                ps.setString(6, text);
                ps.setString(7, text2);
                ps.setString(8, business_id.toString());
                ps.setDate(9, java.sql.Date.valueOf(date_review));
                ps.addBatch();
                
                // Insert data in batch using prepared statements
                if(count %5000 == 4999) {
//                	System.out.println(count);
                	ps.executeBatch();
                	ps.clearBatch();
                    flag=0;
                }
                count++;
    		}
    		if(flag == 1) {
    		    ps.executeBatch();
    		    ps.clearBatch();
                ps.close();
    		}   
  		    
		} catch (ParseException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
    		e.printStackTrace();
    	} catch (SQLException e) {
    		e.printStackTrace();
    	} finally {
    		try {
    			if (br != null)
    				br.close();
    		} catch (IOException ex) {
    			ex.printStackTrace();
    		}
    	} 
    }
    
    // Populate CHECKIN table
    /*public static void PopulateCheckin(BufferedReader br, JSONParser parser, String input)
    {
    	String str="Delete from checkin";
    	jdbc_query(str);
   		str="Delete from checkin_info";
   	    jdbc_query(str);
    	String line1;
    	try {
			br = new BufferedReader(new FileReader(input));
		} catch (FileNotFoundException e1) {
			
			e1.printStackTrace();
		}
    	try {
    		int count=0;
    		int flag=0;
    		while((line1 = br.readLine()) != null) {
    			//For each line in the file
    			Object obj = parser.parse(line1);
				JSONObject jsonObject = (JSONObject) obj;
				String type = (String) jsonObject.get("type");
				String business_id = (String) jsonObject.get("business_id");
				JSONObject checkin_info =(JSONObject)jsonObject.get("checkin_info");
				
				String str2="insert into checkin values(' " + business_id+ " ',' " + type +" ',' " + checkin_info +" ')"; 
				jdbc_query(str2);
				
			
				
				String str1="insert into checkin_info values(?,?,?)";
                if(count == 0) {
                    ps = con.prepareStatement(str1);
                }
                flag=1;
                
				String tmp_str = checkin_info.toString();
				tmp_str = tmp_str.replaceAll("\\{", "");
				tmp_str = tmp_str.replaceAll("\\}", "");
						
				String [] tmp_str2 = tmp_str.split(",");
				for(int i=0; i<tmp_str2.length; i++) {
					tmp_str2[0]=tmp_str2[i].replaceAll("\"","");
					String [] tmp_str3 = tmp_str2[0].split(":");
					Integer num_checkin=Integer.parseInt(tmp_str3[1]);
					String [] tmp_str4 = tmp_str3[0].split("-");
					Integer hr = Integer.parseInt(tmp_str4[0]);
					Integer day = Integer.parseInt(tmp_str4[1]);
					
					Integer var=(24*day+hr);
					ps.setString(1, business_id);
	                ps.setInt(2, var);
	                ps.setInt(3, num_checkin);
					
	                ps.addBatch();
				}
	            if(count %100 == 99) {
//	                System.out.println(count);
	                ps.executeBatch();
	                ps.clearBatch();
	                flag=0;
	            }
	            count++;
	    	}
	    	if(flag == 1) {
	    		ps.executeBatch();
	    		ps.clearBatch();
	            ps.close();
	    		   
			}
				
				
    		
		} catch (ParseException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
    		e.printStackTrace();
    	} catch (SQLException e) {
			
			e.printStackTrace();
		} finally {
    		try {
    			if (br != null)
    				br.close();
    		} catch (IOException ex) {
    			ex.printStackTrace();
    		}
    	}       
    }*/
    
    // Populate YELP_USER table
    public static void PopulateYelpUser(BufferedReader br, JSONParser parser, String input)
    {
        String line2;
        int count=0;
        int flag=0;
        String str1="DELETE FROM YELP_USER";
        
        jdbc_query(str1);
		
		
		
        try {
			br = new BufferedReader(new FileReader(input));
		} catch (FileNotFoundException e1) {
			
			e1.printStackTrace();
		}
		
        
		try {
			
			// For each line in the file
			
			while((line2 = br.readLine()) != null) {
				Object obj = parser.parse(line2);
				JSONObject jsonObject = (JSONObject) obj;
				String yelping_since = (String) jsonObject.get("yelping_since");
	            JSONObject votes =  (JSONObject)jsonObject.get("votes");
				long funny = (long) votes.get("funny");
				long cool = (long) votes.get("cool");
				long useful = (long) votes.get("useful");
				long total_votes = funny + cool + useful;
				long review_count = (long) jsonObject.get("review_count");
                String name = (String)jsonObject.get("name");
				String user_id=(String) jsonObject.get("user_id");
                JSONArray friends = (JSONArray)jsonObject.get("friends");
                long fans = (long) jsonObject.get("fans");
                double average_stars = (double) jsonObject.get("average_stars");
                String type = (String) jsonObject.get("type");
                JSONObject compliments =  (JSONObject)jsonObject.get("compliments");
                JSONArray elite = (JSONArray)jsonObject.get("elite");
                
            
                name = name.replaceAll("'", "''");
                yelping_since += "-01";
             
                int sizee = friends.size();

                String str3="insert into  yelp_user values (?,?,?,?,?,?,?,?,?,?,?)";
                ps = con.prepareStatement(str3);
             
                ps.setDate(1, java.sql.Date.valueOf(yelping_since));
                
                ps.setLong(2, total_votes);
                
                ps.setLong(3, review_count);
                
                ps.setString(4, name);
                
                ps.setString(5, user_id);
                ps.setInt(6, sizee);
                ps.setLong(7, fans);
                
                ps.setDouble(8,average_stars);
                ps.setString(9, type);
                ps.setString(10, String.valueOf(compliments));
                ps.setString(11, String.valueOf(elite));
                
                ps.addBatch();
                if(count %5000 == 4999) {
//                	System.out.println(count);
                	ps.executeBatch();
                	ps.clearBatch();
                    flag=0;
                }
                count++;
    		
    		if(flag == 1) {
    		    ps.executeBatch();
    		    ps.clearBatch();
                ps.close();
    		}   
                ps.executeBatch();
    		    ps.clearBatch();
                ps.close();
			}
   		   
		} catch (ParseException e) {
			
			e.printStackTrace();
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
    	} catch (SQLException e){
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		} 
    }
    
  
    //Populate BUSINESSATTRIBUTES table
    @SuppressWarnings("unchecked")
	public static void PopulateBusinessAttributes(BufferedReader br, JSONParser parser, String input){

    	String line;
        int count=0;
        String str1="DELETE FROM BUSINESSATTRIBUTES";	
        jdbc_query(str1);
		
		
		
        try {
			br = new BufferedReader(new FileReader(input));
		} catch (FileNotFoundException e1) {
			
			e1.printStackTrace();
		}
		
        
		try {
			while((line = br.readLine()) != null) {
				
				// For each line in the file
				Object obj = parser.parse(line);
				JSONObject jsonObject = (JSONObject) obj;
				
				String business_id=(String)jsonObject.get("business_id");
				
                JSONObject attributes=(JSONObject) jsonObject.get("attributes");
                
                
                Set<String> keys = attributes.keySet();
                Object[] bus_atr= keys.toArray();
               
                if(count == 0) {
                	ps = con.prepareStatement("INSERT INTO BusinessAttributes VALUES(?,?,?)");
                }
                String attr_name = "";
                for(int i=0;i<bus_atr.length;i++){
                    Object json =attributes.get(bus_atr[i]);
                    
                    if(json instanceof JSONObject){
                        JSONObject nestedObject=new JSONObject((JSONObject) json);
                        Set<String> keys1 = nestedObject.keySet();
                        Object[] bus_atr2= keys1.toArray();
                        
//                        System.out.println(business_id);
//                        System.out.println(bus_atr[i]);
                        for(int j=0;j<bus_atr2.length;j++){
                        	ps.setString(1, business_id);
//                        	attr_name = attr_name + bus_atr[i]+"_";
//                            ps.setString(2,(String) bus_atr2[j]);
                        	attr_name = (String) bus_atr2[j];//added
                        	
//                        	System.out.println("attribute:"+bus_atr2[j]);
                            if(nestedObject.get(bus_atr2[j]).equals(true)){
                            	
                            	ps.setString(2,bus_atr[i]+"_"+attr_name.concat("_true"));//added
                                ps.setString(3,"true");}
                                else{
                                	ps.setString(2,bus_atr[i]+"_"+attr_name.concat("_false"));
                                	ps.setString(3,"false");}
                            ps.addBatch();
                            count+=1;
                        }
                    }
                    else{
                    	ps.setString(1, business_id);
                        //ps.setString(2,(String) bus_atr[i]);
                        if(attributes.get(bus_atr[i]).equals(true)){
                        	ps.setString(2,bus_atr[i]+"_true");
                        	ps.setString(3,"true");}
                        else{
                        	ps.setString(2,bus_atr[i]+"_false");
                        	ps.setString(3,"false");}
                        	ps.addBatch();
                        	count+=1;
                    	}
                   
                    
                    
                    
                    if(count%5000==0){
                    ps.executeBatch();
                    count=0;
                }
             }
        }
                ps.executeBatch();
                ps.close();
    }
         catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }catch (IOException ex) {
            ex.printStackTrace();
        } catch (ParseException ex) {
            ex.printStackTrace();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
        catch (SQLException E) {
     	System.out.println("SQLException: " + E.getMessage());
     	System.out.println("SQLState: " + E.getSQLState());
     	System.out.println("VendorError:" + E.getErrorCode());
     	}
    }
		
	// Populate BUSINESS_CATEGORY table	
    public static void PopulateBusinessCategory(BufferedReader br, JSONParser parser, String input)
    {
    	
    	
		String str2="Delete from business_category";
		
		
		jdbc_query(str2);
		
	    String line;
	    int count = 0;
		try {
			br = new BufferedReader(new FileReader(input));
		} catch (FileNotFoundException e1) {
			
			e1.printStackTrace();
		}
		try {
	           // Insert business categories in hashmap
	           HashMap<String,Object> main_categories=new HashMap<String,Object>();
	           main_categories.put("Active Life", true);
	           main_categories.put("Arts & Entertainment", true);
	           main_categories.put("Automotive", true);
	           main_categories.put("Car Rental", true);
	           main_categories.put("Cafes", true);
	           main_categories.put("Beauty & Spas", true);
	           main_categories.put("Convenience Stores", true);
	           main_categories.put("Dentists", true);
	           main_categories.put("Doctors", true);
	           main_categories.put("Drugstores", true);
	           main_categories.put("Department Stores", true);
	           main_categories.put("Education", true);
	           main_categories.put("Event Planning & Services", true);
	           main_categories.put("Flowers & Gifts", true);
	           main_categories.put("Food", true);
	           main_categories.put("Health & Medical", true);
	           main_categories.put("Home Services", true);
	           main_categories.put("Home & Garden", true);
	           main_categories.put("Hospitals", true);
	           main_categories.put("Hotels & Travel", true);
	           main_categories.put("Hardware Stores", true);
	           main_categories.put("Grocery", true);
	           main_categories.put("Medical Centers", true);
	           main_categories.put("Nurseries & Gardening", true);
	           main_categories.put("Nightlife", true);
	           main_categories.put("Restaurants", true);
	           main_categories.put("Shopping", true);
	           main_categories.put("Transportation", true);
	           


	           PreparedStatement s = con.prepareStatement("INSERT INTO business_category VALUES(?,?,?)");
	           while((line = br.readLine()) != null)  {
	        	   
	               List<String> business_category=new ArrayList<String>();
	               List<String> sub_category=new ArrayList<String>();
	               Object obj = parser.parse(line);
					JSONObject jsonObject = (JSONObject) obj;

	               String businessID = (String) jsonObject.get("business_id");
	               s.setString(1, businessID);
	               JSONArray categories= (JSONArray) jsonObject.get("categories");
	               for(int i=0;i<categories.size();i++){
	                   if(main_categories.containsKey(categories.get(i))){
	                       business_category.add((String) categories.get(i));
	                   }
	                   else{
	                       sub_category.add((String) categories.get(i));
	                   }
	               }
	               
	               for(String category : business_category){
	                   s.setString(2, category);
	                   if(sub_category.isEmpty()){
	                       s.setString(3, "NULL");
	                       s.addBatch();
	                       count+=1;
	                   }
	                   else{  
	                       for(String sub_cat : sub_category){
	                           s.setString(3, sub_cat);
	                           s.addBatch();
	                           count+=1;
	                       }
	                   }
	               }
	               if(count%5000==0){
	                   s.executeBatch();
	                   count=0;
	               }
	                           
	            }
	           
	           s.executeBatch();
	           s.close();
	   }
	       catch (FileNotFoundException ex) {
	           ex.printStackTrace();
	       }catch (IOException ex) {
	           ex.printStackTrace();
	       } catch (ParseException ex) {
	           ex.printStackTrace();
	       } catch (NullPointerException ex) {
	           ex.printStackTrace();
	       }
	       catch (SQLException E) {
	      System.out.println("SQLException: " + E.getMessage());
	      System.out.println("SQLState: " + E.getSQLState());
	      System.out.println("VendorError:" + E.getErrorCode());
	       }

		
		
	}
    
    // Populate BUSINESS table
    public static void PopulateBusiness(BufferedReader br, JSONParser parser, String input)
    {
    	int count=0;int flag=0;
    	Hashtable<String, ArrayList<String>> hashtable = 
                new Hashtable<String, ArrayList<String>>();
    	String[] business_category={"Active Life","Arts & Entertainment","Automotive","Car Rental","Cafes","Beauty & Spas","Convenience Stores",
    			"Dentists","Doctors","Drugstores","Department Stores","Education","Event Planning &Services","Flowers & gifts","Food","Health and Medical"
    			,"Home Services","Home and Garden","Hospitals","Hotels and Travel","Hardware Stores","Grocery","Medical Centers","Nurseries & Gardening","Nightlife",
    			"Restaurants","Shopping","Transportation"};
    	for(int j=0;j<business_category.length;j++)
    	{
    		ArrayList<String> list1=new ArrayList<String>();
    		hashtable.put(business_category[j],list1);
    	}
    	
    	String str1="Delete from business";
    	jdbc_query(str1);
		
	    String line;
		try {
			br = new BufferedReader(new FileReader(input));
		} catch (FileNotFoundException e1) {
			
			e1.printStackTrace();
		}
		
		ArrayList<String> listCat = new ArrayList<String>();
		ArrayList<String> listSubCat = new ArrayList<String>();
		try {
			while((line = br.readLine()) != null) {
				Object obj = parser.parse(line);
				JSONObject jsonObject = (JSONObject) obj;
				
				String business_id=(String)jsonObject.get("business_id");
				String full_address = (String)jsonObject.get("full_address");
				JSONObject hours=(JSONObject) jsonObject.get("hours");
				Boolean open = (Boolean) jsonObject.get("open");
				JSONArray categories= (JSONArray)jsonObject.get("categories");
				long review_count = (long)jsonObject.get("review_count");
                String city=(String)jsonObject.get("city");
                String state=(String) jsonObject.get("state");
                double latitude=(double) jsonObject.get("latitude");
                double longitude=(double) jsonObject.get("longitude");
                String name=(String)jsonObject.get("name");
                JSONArray neighborhoods=(JSONArray) jsonObject.get("neighborhoods");
                double stars=(double) jsonObject.get("stars");
                JSONObject attributes=(JSONObject) jsonObject.get("attributes");
                String type=(String) jsonObject.get("type");
          
                for (int k = 0; k < categories.size(); k++) {
         			if(hashtable.containsKey(categories.get(k))) {
         				listCat.add((String)(categories.get(k)));
         			} else {
         				listSubCat.add((String)(categories.get(k)));
         			}
         			
         		}
                
                for (int k = 0; k < listCat.size(); k++) {
         			for(int j=0; j<listSubCat.size();j++) {
         				ArrayList <String>temp = hashtable.get(listCat.get(k));
         				if(!temp.contains(listSubCat.get(j))) {
         					temp.add(listSubCat.get(j));
                	   	    	hashtable.put((String)listCat.get(k), temp);
         				}
         			
         			}
         		}
                listCat.clear();
                listSubCat.clear();
                
                
		    
		   
		    
		    str1="insert into  business values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            if(count == 0) {
            	ps = con.prepareStatement(str1);
            }
            
            flag=1;
            ps.setString(1, business_id);
            ps.setString(2, full_address);
            ps.setString(3, String.valueOf(hours));
            ps.setString(4,String.valueOf(open));
            ps.setString(5,String.valueOf(categories));
            ps.setLong(6, review_count);
            ps.setString(7, city);
            ps.setString(8, state);
            ps.setDouble(9, latitude);
            ps.setDouble(10,longitude);
            ps.setString(11,name);
            ps.setString(12, String.valueOf(neighborhoods));
            ps.setDouble(13,stars);
            ps.setString(14,String.valueOf(attributes));
            ps.setString(15, type);
           
            ps.addBatch();
            if(count %5000 == 4999) {
//            	System.out.println(count);
            	ps.executeBatch();
            	ps.clearBatch();
            	flag=0;
            }
            count++;
        }	
	    if(flag == 1) {
	    	ps.executeBatch();
	    	ps.clearBatch();
            ps.close();
	    }
    	} catch (ParseException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e){
			e.printStackTrace();
		}finally {
		
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		} 
	}
		 
     
}