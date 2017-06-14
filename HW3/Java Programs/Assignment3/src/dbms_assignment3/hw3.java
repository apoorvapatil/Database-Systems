/**
 * 
 *
 * @author Apoorva
 * 
 * 
 */

package dbms_assignment3;

import javax.swing.*;
import java.sql.*;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import java.text.*;
import javax.swing.table.TableColumn;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.LineBorder;


@SuppressWarnings("serial")
public class hw3 extends javax.swing.JFrame implements ItemListener {

    /**
     * HW3
     */
static int count=0;
public static Connection con = null;
public static Statement stmt = null;
public static ResultSet r1,r2, r3;
public static ResultSet rs,rs1;
public static  String business_category;
public static ResultSetMetaData rsmetadata;
int columns;
String review_query;
String value;

	// Constructor
    public hw3() {
    	getContentPane().setBackground(new Color(230, 230, 250));
        initComponents();
        start();
    }
    
    // Global variables
    String str_cat;
    ArrayList <String> subcategory_list=new ArrayList<String>();
    ArrayList<String> category_list=new ArrayList<String>();
    ArrayList<String> attribute_list=new ArrayList<String>();
    ArrayList<String> set=new ArrayList<String>();
    String str3;
    String str_subcat, str_atr;
    String query;
    javax.swing.JPanel jPanel_cust1 = new javax.swing.JPanel();
    Integer index_combo;
     Integer from_hour1,combo_from;
      Integer index_combo1;
     Integer to_hour1,combo_to;
     Integer checkin_value1;
     JScrollPane attributes = new JScrollPane();
     
    // Item Listener for items checked in Category, Subcategory and attributes 
    public void itemStateChanged(ItemEvent ie) {
    
    // Populates sub categories corresponding to categories selected	
    javax.swing.JCheckBox chosen = (javax.swing.JCheckBox) ie.getItem();

    if (chosen.getName().equals("category")){
		
    	// Items selected are added in the array list
	    if(chosen.isSelected()) {
	        category_list.add(chosen.getText());
	        subcategory_list.clear();
	    }
	   else
	    {
	        category_list.remove(chosen.getText());
	        subcategory_list.clear();
	    }
	    jPanel_cust1.removeAll();
	    jPanel_cust1.repaint();
	    set.clear();           
	    
	    // Query to get sub categories from categories selected
	    str3="select BUS_CATG_NAME from business_category where BUS_CATG_ID like ";
	    for(int i=0;i<category_list.size();i++){
	        if(i!=0)
	        	str3= str3 + " or BUS_CATG_ID like ";
	        str3= str3 + "'%" + category_list.get(i).trim() + "%'";
	    }
	    
	    // Get distinct sub categories
	    String str4="select distinct BUS_CATG_NAME from business_category WHERE BUS_CATG_NAME in (";
	    str4 = str4 + str3;
	    str4 = str4 + ")";
	    System.out.println(str4);
	    r2 =jdbc_connectivity(str4);
	    
	    
	    // Addsub categories to the sub category panel
	    ArrayList<String> subcat=new ArrayList<String>();
	    try {
	        while(r2.next())
	        {
	        	subcat.add(r2.getString("BUS_CATG_NAME"));
	        }
	        int sizesub=subcat.size();
	        javax.swing.JCheckBox[] c3 = new javax.swing.JCheckBox[sizesub];
	    
	        javax.swing.JPanel jPanel_cust1 = new javax.swing.JPanel();
	        jPanel_cust1.setBackground(new Color(224, 255, 255));
	        jPanel_cust1.setLayout(new BoxLayout(jPanel_cust1, BoxLayout.Y_AXIS));
	        subcategory.setPreferredSize(new Dimension(50,250));
	   
	        for(int i=0;i<sizesub;i++)
	        {
	            c3[i] = new javax.swing.JCheckBox((subcat.get(i)));
	            c3[i].setName("subcategory");
	            
	            c3[i].addItemListener(this);
	            jPanel_cust1.add(c3[i]);
	            
	        }
	        subcategory.setViewportView(jPanel_cust1);
	    } catch (SQLException ex) {
	        Logger.getLogger(hw3.class.getName()).log(Level.SEVERE, null, ex);
	    }
	   
	}else
	{
	if(chosen.isSelected()) {
	    subcategory_list.add(chosen.getText());
	}
	else
	{
	    subcategory_list.remove(chosen.getText());

	}

	}
	    
    			// Populates attributes corresponding to sub categories selected
	    		String str_atr;
	    		javax.swing.JCheckBox chosenatr = (javax.swing.JCheckBox) ie.getItem();
	    		
	    		
	    		if (chosenatr.getName().equals("subcategory")){
	    				
	    				// Query to get attributes for the selected sub categories
	    	            str_atr="select distinct BUS_ATTRIBUTE from BUSINESSATTRIBUTES where business_id in( select business_id from business_CATEGORY where (BUS_CATG_ID like ";
	    	            for(int i=0;i<category_list.size();i++){
	    	                if(i!=0)
	    	                	str_atr= str_atr + " or BUS_CATG_ID like ";
	    	                str_atr= str_atr + "'%" + category_list.get(i).trim() + "%'";
	    	            }
	    	            str_atr=str_atr+" ) and (BUS_CATG_NAME like ";
	    	            for(int i=0;i<subcategory_list.size();i++)
	    	            {
	    	                if(i != 0)
	    	                	str_atr = str_atr + " or BUS_CATG_NAME like ";
	    	            
	    	                str_atr= str_atr + "'%" + subcategory_list.get(i).trim() + "%'";
	    	            }
	    	     
	    	            str_atr = str_atr + "))";
	    	            System.out.println(str_atr);
	    	            r3 =jdbc_connectivity(str_atr);
	    	            
	    	            // Add attributes to the attributes panel
	    	            ArrayList<String> attribute=new ArrayList<String>();
	    	            try {
	    	                while(r3.next())
	    	                {
	    	                	attribute.add(r3.getString("BUS_ATTRIBUTE"));
	    	                }
	    	                int sizee=attribute.size();
	    	                javax.swing.JCheckBox[] c2 = new javax.swing.JCheckBox[sizee];
	    	            
	    	                javax.swing.JPanel jPanel_attr = new javax.swing.JPanel();
	    	                jPanel_attr.setBackground(new Color(224, 255, 255));
	    	                jPanel_attr.setLayout(new BoxLayout(jPanel_attr, BoxLayout.Y_AXIS));
	    	                attributes.setPreferredSize(new Dimension(50,250));
	    	           
	    	                for(int i=0;i<sizee;i++)
	    	                {
	    	                    c2[i] = new javax.swing.JCheckBox((attribute.get(i)));
	    	                    c2[i].setName("attributes");
	    	                    
	    	                    c2[i].addItemListener(this);
	    	                    jPanel_attr.add(c2[i]);
	    	                    
	    	                }
	    	                attributes.setViewportView(jPanel_attr);
	    	            } catch (SQLException ex) {
	    	                Logger.getLogger(hw3.class.getName()).log(Level.SEVERE, null, ex);
	    	            }
	    		 }
	    		 else
	    	        {
	    				 if(chosenatr.isSelected()) {
	    					 
	    					 attribute_list.add(chosenatr.getText());
	    					
	    					 
	    				 }
	    			 
	        	        else
	        	        {
	        	        	
	        	        	attribute_list.remove(chosenatr.getText());

	        	        }
	    			 }

    }

      
    
    public  void start() {
    	
    	// Populates categories when the GUI starts
       String str1="SELECT DISTINCT BUS_CATG_ID FROM BUSINESS_CATEGORY";
        r1 =jdbc_connectivity(str1);
        display_checkbox(r1);
        revalidate();
    }
    
    // Connection to Oracle 11g
    public static ResultSet jdbc_connectivity(String str)
    {
        try{  
	    // Load the driver class
	    if(count==0){
                Class.forName("oracle.jdbc.driver.OracleDriver");  
	              
	        // Create connection object  
                con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521/orcl","hr","oracle");
                count++;
                stmt=con.createStatement();
            }
	    rs=stmt.executeQuery(str);
              
	} catch(Exception e)
        { 
            System.out.println(e);
            System.exit(0);
	} 
        return (rs);
	        
    }
    
    // Display/Add categories to categories panel
    public  void display_checkbox(ResultSet r1)
    {
        ArrayList<String> business=new ArrayList<String>();
        try {
            while(r1.next())
            {
                business.add(r1.getString("BUS_CATG_ID"));
            }
            int size=business.size();
            javax.swing.JCheckBox[] c1 = new javax.swing.JCheckBox[size];
        
            javax.swing.JPanel jPanel_cust = new javax.swing.JPanel();
            jPanel_cust.setBackground(new Color(224, 255, 255));
            jPanel_cust.setLayout(new BoxLayout(jPanel_cust, BoxLayout.Y_AXIS));
            category.setPreferredSize(new Dimension(50,250));
       
            for(int i=0;i<size;i++)
            {
                c1[i] = new javax.swing.JCheckBox((business.get(i)));
                c1[i].setName("category");
                c1[i].addItemListener(this);
                jPanel_cust.add(c1[i]);
            }
            category.setViewportView(jPanel_cust);
        } catch (SQLException ex) {
            Logger.getLogger(hw3.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // Initialize GUI components
    @SuppressWarnings({ "deprecation", "rawtypes", "unchecked" })
	private void initComponents() {
        jPanel4 = new javax.swing.JPanel();
        jPanel4.setBackground(new Color(224, 255, 255));
        userreview_count = new javax.swing.JComboBox<>();
        userfriend_list = new javax.swing.JComboBox<>();
        useravg_star = new javax.swing.JComboBox<>();
        userno_votes = new JComboBox<String>();
        user_and = new javax.swing.JComboBox<>();
        user_and.setForeground(new Color(255, 0, 255));
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel20.setFont(new Font("Lucida Grande", Font.BOLD | Font.ITALIC, 13));
        jLabel20.setForeground(new Color(138, 43, 226));
        
        // Yelp user components
        user_count_value = new javax.swing.JTextField();
        user_friend_value = new javax.swing.JTextField();
        user_star_value = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jLabel21.setForeground(new Color(255, 0, 255));
        jLabel22 = new javax.swing.JLabel();
        jLabel22.setForeground(new Color(255, 0, 255));
        jLabel22.setBackground(new Color(255, 0, 255));
        jLabel23 = new javax.swing.JLabel();
        jLabel23.setForeground(new Color(255, 0, 255));
        jLabel24 = new javax.swing.JLabel();
        jLabel24.setForeground(new Color(255, 0, 255));
        jLabel25 = new javax.swing.JLabel();
        jLabel25.setForeground(new Color(255, 0, 255));
        jLabel25.setBackground(new Color(255, 0, 255));
        yelpuser_date = new com.toedter.calendar.JDateChooser();
        jPanel_5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jScrollPane1.setBorder(new LineBorder(new Color(255, 20, 147), 2));
        
        // Display component for query
        query_display = new javax.swing.JTextArea();
        query_display.setText("<Enter your query here>");
        query_display.setBackground(new Color(224, 255, 255));
        Query_button = new javax.swing.JButton();
        Query_button.setForeground(new Color(255, 0, 255));
        Query_button.setBackground(new Color(221, 160, 221));
        jLabel1 = new javax.swing.JLabel();
        jLabel1.setFont(new Font("Oriya MN", Font.PLAIN, 20));
        jLabel1.setForeground(new Color(220, 20, 60));
        jLabel1.setBackground(new Color(65, 105, 225));
        jLabel2 = new javax.swing.JLabel();
        jLabel2.setForeground(new Color(138, 43, 226));
        jLabel2.setBackground(new Color(65, 105, 225));
        jLabel2.setFont(new Font("Lucida Grande", Font.BOLD | Font.ITALIC, 13));
        
        // JScrollPanels
        category = new javax.swing.JScrollPane();
        subcategory = new javax.swing.JScrollPane();
        jPanel_1 = new javax.swing.JPanel();
        jPanel_1.setBackground(new Color(224, 255, 255));
        reviewstar_count = new javax.swing.JComboBox<>();
        reviewstar_count.setBackground(UIManager.getColor("ComboBox.background"));
        reviewno_votes = new javax.swing.JComboBox<>();
        reviewno_votes.setBackground(UIManager.getColor("ComboBox.background"));
        jLabel8 = new javax.swing.JLabel();
        jLabel8.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
        jLabel8.setForeground(new Color(255, 0, 255));
        jLabel9 = new javax.swing.JLabel();
        jLabel9.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
        jLabel9.setForeground(new Color(255, 0, 255));
        jLabel10 = new javax.swing.JLabel();
        jLabel10.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
        jLabel10.setForeground(new Color(255, 0, 255));
        
        // Review components
        review_star_value = new javax.swing.JTextField();
        review_star_value.setBackground(new Color(255, 255, 255));
        review_votes_value = new javax.swing.JTextField();
        review_votes_value.setBackground(new Color(255, 255, 255));
        jLabel11 = new javax.swing.JLabel();
        jLabel11.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
        jLabel11.setForeground(new Color(255, 0, 255));
        jLabel12 = new javax.swing.JLabel();
        jLabel12.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
        jLabel12.setForeground(new Color(255, 0, 255));
        jLabel13 = new javax.swing.JLabel();
        jLabel13.setForeground(new Color(255, 0, 255));
        jLabel14 = new javax.swing.JLabel();
        jLabel14.setFont(new Font("Lucida Grande", Font.BOLD | Font.ITALIC, 13));
        jLabel14.setForeground(new Color(138, 43, 226));
        fromreview_date = new com.toedter.calendar.JDateChooser();
        toreview_date = new com.toedter.calendar.JDateChooser();
        
        // Result pane
        bus_user_resultset = new javax.swing.JScrollPane();
        bus_user_resultset.setBackground(new Color(224, 255, 255));
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setForeground(new java.awt.Color(102, 102, 255));
        // Radio button group
        ButtonGroup bg = new ButtonGroup();
        
        user_radio = new JRadioButton("");
        business_radio = new JRadioButton("");
        business_radio.setSelected(true);
        bg.add(user_radio);
        bg.add(business_radio);
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        userreview_count.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { ">", "=", "<" }));
        userreview_count.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                user_countActionPerformed(evt);
            }
        });

        userfriend_list.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { ">", "=", "<" }));
        userfriend_list.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                user_friendActionPerformed(evt);
            }
        });

        useravg_star.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { ">", "=", "<" }));

        userno_votes.setModel(new DefaultComboBoxModel(new String[] {">", "=", "<"}));
        user_and.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "AND", "OR" }));
        user_and.setBorder(new LineBorder(new Color(224, 255, 255), 2));
        user_and.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                user_andActionPerformed(evt);
            }
        });

        // Labels
        jLabel17.setText("VALUE");

        jLabel18.setText("VALUE");

        jLabel19.setText("VALUE");

        jLabel20.setText("USERS");

        user_count_value.setText("0");

        user_friend_value.setText("0");

        user_star_value.setText("0");

        jLabel21.setText("Review count");

        jLabel22.setText("No. Of Friends");

        jLabel23.setText("Avg Stars");

        jLabel24.setText("No of votes");

        jLabel25.setText("Member Since");

        yelpuser_date.setDateFormatString("yyyy-MMM-dd");
        
       
        
        JLabel label = new JLabel();
        label.setText("VALUE");
        
        textField = new JTextField();
        textField.setText("0");
        
        

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4Layout.setHorizontalGroup(
        	jPanel4Layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(jPanel4Layout.createSequentialGroup()
        			.addGap(20)
        			.addGroup(jPanel4Layout.createParallelGroup(Alignment.LEADING)
        				.addGroup(jPanel4Layout.createSequentialGroup()
        					.addGroup(jPanel4Layout.createParallelGroup(Alignment.LEADING)
        						.addComponent(jLabel24)
        						.addComponent(jLabel23)
        						.addComponent(jLabel22, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE)
        						.addComponent(jLabel21, GroupLayout.PREFERRED_SIZE, 98, GroupLayout.PREFERRED_SIZE))
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addGroup(jPanel4Layout.createParallelGroup(Alignment.LEADING)
        						.addComponent(userfriend_list, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        						.addComponent(userreview_count, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        						.addComponent(useravg_star, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        						.addComponent(userno_votes, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE)))
        				.addComponent(jLabel25, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE))
        			.addPreferredGap(ComponentPlacement.RELATED, 54, Short.MAX_VALUE)
        			.addGroup(jPanel4Layout.createParallelGroup(Alignment.TRAILING)
        				.addGroup(jPanel4Layout.createSequentialGroup()
        					.addGroup(jPanel4Layout.createParallelGroup(Alignment.LEADING, false)
        						.addGroup(jPanel4Layout.createSequentialGroup()
        							.addGroup(jPanel4Layout.createParallelGroup(Alignment.LEADING)
        								.addComponent(jLabel17)
        								.addComponent(jLabel18)
        								.addComponent(label, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
        							.addPreferredGap(ComponentPlacement.UNRELATED)
        							.addGroup(jPanel4Layout.createParallelGroup(Alignment.LEADING, false)
        								.addComponent(textField)
        								.addComponent(user_friend_value)
        								.addComponent(user_count_value, GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE)))
        						.addGroup(jPanel4Layout.createSequentialGroup()
        							.addComponent(jLabel19)
        							.addPreferredGap(ComponentPlacement.UNRELATED)
        							.addComponent(user_star_value)))
        					.addGap(28))
        				.addGroup(jPanel4Layout.createSequentialGroup()
        					.addComponent(yelpuser_date, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        					.addGap(22))))
        		.addGroup(jPanel4Layout.createSequentialGroup()
        			.addGap(137)
        			.addComponent(user_radio)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(jLabel20)
        			.addGap(199))
        		.addGroup(jPanel4Layout.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(user_and, 0, 397, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
        	jPanel4Layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(jPanel4Layout.createSequentialGroup()
        			.addGap(6)
        			.addGroup(jPanel4Layout.createParallelGroup(Alignment.TRAILING, false)
        				.addGroup(jPanel4Layout.createSequentialGroup()
        					.addComponent(user_radio, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
        					.addGap(11)
        					.addComponent(jLabel25, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
        					.addGap(24))
        				.addGroup(jPanel4Layout.createSequentialGroup()
        					.addComponent(jLabel20)
        					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        					.addComponent(yelpuser_date, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        					.addGap(18)))
        			.addGroup(jPanel4Layout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(jLabel21)
        				.addComponent(userreview_count, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(jLabel17)
        				.addComponent(user_count_value, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE))
        			.addGap(18)
        			.addGroup(jPanel4Layout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(jLabel22)
        				.addComponent(userfriend_list, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(jLabel18)
        				.addComponent(user_friend_value, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addGroup(jPanel4Layout.createParallelGroup(Alignment.LEADING)
        				.addGroup(jPanel4Layout.createSequentialGroup()
        					.addGap(11)
        					.addGroup(jPanel4Layout.createParallelGroup(Alignment.BASELINE)
        						.addComponent(jLabel23)
        						.addComponent(useravg_star, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addGroup(jPanel4Layout.createParallelGroup(Alignment.BASELINE)
        						.addComponent(jLabel24)
        						.addComponent(label)
        						.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        						.addComponent(userno_votes, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
        				.addGroup(jPanel4Layout.createSequentialGroup()
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addGroup(jPanel4Layout.createParallelGroup(Alignment.BASELINE)
        						.addComponent(jLabel19)
        						.addComponent(user_star_value, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(user_and, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        			.addGap(86))
        );
        jPanel4.setLayout(jPanel4Layout);
        jPanel4.setBorder(new LineBorder(new Color(255, 20, 147), 2));
        jPanel_5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jPanel_5.setBorder(new LineBorder(new Color(255, 20, 147), 2));
        query_display.setColumns(20);
        query_display.setLineWrap(true);
        query_display.setRows(5);
        query_display.setBorder(new LineBorder(new Color(255, 20, 147), 2));
        jScrollPane1.setViewportView(query_display);

        javax.swing.GroupLayout gl_jPanel_5 = new javax.swing.GroupLayout(jPanel_5);
        jPanel_5.setLayout(gl_jPanel_5);
        gl_jPanel_5.setHorizontalGroup(
            gl_jPanel_5.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        gl_jPanel_5.setVerticalGroup(
            gl_jPanel_5.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
        );

        Query_button.setBorder(new LineBorder(new Color(255, 20, 147), 2));
        Query_button.setLabel("Execute Query");
        Query_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Query_buttonActionPerformed(evt);
            }
        });

        jLabel1.setText("YELP APPLICATION");

        jLabel2.setText("BUSINESS");

        category.setBorder(new LineBorder(new Color(255, 20, 147), 2));
        category.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        category.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        subcategory.setBorder(new LineBorder(new Color(255, 20, 147), 2));
        subcategory.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        subcategory.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        attributes.setBorder(new LineBorder(new Color(255, 20, 147), 2));
        attributes.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        attributes.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        
        jPanel_1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jPanel_1.setBorder(new LineBorder(new Color(255, 20, 147), 2));
        reviewstar_count.setModel(new DefaultComboBoxModel(new String[] {">", "=", "<"}));
        reviewstar_count.setBorder(new LineBorder(new Color(224, 255, 255), 2));
        reviewstar_count.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                review_starActionPerformed(evt);
            }
        });

        reviewno_votes.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { ">", "=", "<" }));
        reviewno_votes.setBorder(new LineBorder(new Color(224, 255, 255), 2));
        reviewno_votes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                review_votesActionPerformed(evt);
            }
        });

        jLabel8.setText("From");

        jLabel9.setText("To");

        jLabel10.setText("No of Stars");

        review_star_value.setText("0");
        review_star_value.setBorder(new LineBorder(new Color(0, 0, 0)));
        review_star_value.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                review_star_valueActionPerformed(evt);
            }
        });

        review_votes_value.setText("0");
        review_votes_value.setBorder(new LineBorder(new Color(0, 0, 0)));

        jLabel11.setText("Value");

        jLabel12.setText("No of Votes");

        jLabel13.setText("Value");

        jLabel14.setText("REVIEWS");

        fromreview_date.setDateFormatString("yyyy-MMM-dd");

        toreview_date.setDateFormatString("yyyy-MMM-dd");

        javax.swing.GroupLayout gl_jPanel_1 = new javax.swing.GroupLayout(jPanel_1);
        gl_jPanel_1.setHorizontalGroup(
        	gl_jPanel_1.createParallelGroup(Alignment.LEADING)
        		.addGroup(Alignment.TRAILING, gl_jPanel_1.createSequentialGroup()
        			.addContainerGap(99, Short.MAX_VALUE)
        			.addComponent(jLabel14, GroupLayout.PREFERRED_SIZE, 92, GroupLayout.PREFERRED_SIZE)
        			.addGap(93))
        		.addGroup(gl_jPanel_1.createSequentialGroup()
        			.addGroup(gl_jPanel_1.createParallelGroup(Alignment.LEADING)
        				.addGroup(gl_jPanel_1.createSequentialGroup()
        					.addGroup(gl_jPanel_1.createParallelGroup(Alignment.LEADING)
        						.addGroup(gl_jPanel_1.createSequentialGroup()
        							.addGap(6)
        							.addGroup(gl_jPanel_1.createParallelGroup(Alignment.LEADING)
        								.addGroup(gl_jPanel_1.createSequentialGroup()
        									.addGap(6)
        									.addComponent(jLabel10, GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE))
        								.addGroup(gl_jPanel_1.createSequentialGroup()
        									.addGap(25)
        									.addComponent(jLabel11, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)))
        							.addPreferredGap(ComponentPlacement.RELATED))
        						.addGroup(gl_jPanel_1.createSequentialGroup()
        							.addGap(19)
        							.addGroup(gl_jPanel_1.createParallelGroup(Alignment.TRAILING)
        								.addComponent(jLabel9, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
        								.addComponent(jLabel8, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE))
        							.addPreferredGap(ComponentPlacement.RELATED)))
        					.addGroup(gl_jPanel_1.createParallelGroup(Alignment.LEADING)
        						.addGroup(gl_jPanel_1.createParallelGroup(Alignment.TRAILING)
        							.addComponent(review_star_value, GroupLayout.PREFERRED_SIZE, 58, GroupLayout.PREFERRED_SIZE)
        							.addComponent(reviewstar_count, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE))
        						.addGroup(gl_jPanel_1.createSequentialGroup()
        							.addPreferredGap(ComponentPlacement.UNRELATED)
        							.addComponent(fromreview_date, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        						.addComponent(toreview_date, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
        				.addGroup(gl_jPanel_1.createParallelGroup(Alignment.TRAILING, false)
        					.addGroup(Alignment.LEADING, gl_jPanel_1.createSequentialGroup()
        						.addGap(41)
        						.addComponent(jLabel13, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)
        						.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        						.addComponent(review_votes_value, GroupLayout.PREFERRED_SIZE, 54, GroupLayout.PREFERRED_SIZE))
        					.addGroup(Alignment.LEADING, gl_jPanel_1.createSequentialGroup()
        						.addGap(15)
        						.addComponent(jLabel12, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
        						.addPreferredGap(ComponentPlacement.RELATED)
        						.addComponent(reviewno_votes, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE))))
        			.addGap(48))
        );
        gl_jPanel_1.setVerticalGroup(
        	gl_jPanel_1.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_jPanel_1.createSequentialGroup()
        			.addGroup(gl_jPanel_1.createParallelGroup(Alignment.TRAILING)
        				.addGroup(gl_jPanel_1.createSequentialGroup()
        					.addComponent(jLabel14, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(fromreview_date, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        				.addComponent(jLabel8, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE))
        			.addGap(30)
        			.addGroup(gl_jPanel_1.createParallelGroup(Alignment.TRAILING)
        				.addComponent(toreview_date, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(jLabel9, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE))
        			.addGap(18)
        			.addGroup(gl_jPanel_1.createParallelGroup(Alignment.BASELINE)
        				.addComponent(jLabel10, GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)
        				.addComponent(reviewstar_count, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(gl_jPanel_1.createParallelGroup(Alignment.LEADING)
        				.addGroup(Alignment.TRAILING, gl_jPanel_1.createSequentialGroup()
        					.addGroup(gl_jPanel_1.createParallelGroup(Alignment.BASELINE)
        						.addComponent(jLabel12, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
        						.addComponent(reviewno_votes, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE))
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addGroup(gl_jPanel_1.createParallelGroup(Alignment.BASELINE)
        						.addComponent(jLabel13, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
        						.addComponent(review_votes_value, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE))
        					.addGap(54))
        				.addGroup(Alignment.TRAILING, gl_jPanel_1.createSequentialGroup()
        					.addGroup(gl_jPanel_1.createParallelGroup(Alignment.BASELINE)
        						.addComponent(jLabel11)
        						.addComponent(review_star_value, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE))
        					.addGap(140))))
        );
        jPanel_1.setLayout(gl_jPanel_1);

        bus_user_resultset.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        bus_user_resultset.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        bus_user_resultset.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        bus_user_resultset.setBorder(new LineBorder(new Color(255, 20, 147), 2));
        
        business_and = new JComboBox<String>();
        business_and.setModel(new DefaultComboBoxModel(new String[] {"AND", "OR"}));
        business_and.setForeground(Color.MAGENTA);
        business_and.setBorder(new LineBorder(new Color(230, 230, 250), 2));
        
        lblResults = new JLabel();
        lblResults.setText("RESULTS");
        lblResults.setForeground(new Color(138, 43, 226));
        lblResults.setFont(new Font("Lucida Grande", Font.BOLD | Font.ITALIC, 13));
        lblResults.setBackground(new Color(65, 105, 225));
        
        JButton quit_app = new JButton("Quit Application");
        quit_app.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		System.out.println("Quitting yelp application");
        		System.exit(0);
        	}
        });
        quit_app.setForeground(new Color(255, 0, 255));
        quit_app.setBackground(new Color(221, 160, 221));
        quit_app.setBorder(new LineBorder(new Color(255, 20, 147), 2));
        
        

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        layout.setHorizontalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addGroup(layout.createParallelGroup(Alignment.LEADING)
        				.addGroup(layout.createSequentialGroup()
        					.addContainerGap()
        					.addGroup(layout.createParallelGroup(Alignment.LEADING)
        						.addGroup(layout.createParallelGroup(Alignment.TRAILING)
        							.addComponent(jPanel4, GroupLayout.PREFERRED_SIZE, 407, GroupLayout.PREFERRED_SIZE)
        							.addComponent(business_and, GroupLayout.PREFERRED_SIZE, 397, GroupLayout.PREFERRED_SIZE))
        						.addGroup(layout.createSequentialGroup()
        							.addComponent(category, GroupLayout.PREFERRED_SIZE, 127, GroupLayout.PREFERRED_SIZE)
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addGroup(layout.createParallelGroup(Alignment.LEADING)
        								.addComponent(business_radio)
        								.addGroup(layout.createSequentialGroup()
        									.addComponent(subcategory, GroupLayout.PREFERRED_SIZE, 127, GroupLayout.PREFERRED_SIZE)
        									.addPreferredGap(ComponentPlacement.UNRELATED)
        									.addComponent(attributes, GroupLayout.PREFERRED_SIZE, 141, GroupLayout.PREFERRED_SIZE))))))
        				.addGroup(layout.createSequentialGroup()
        					.addGap(166)
        					.addComponent(jLabel2, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE)))
        			.addGroup(layout.createParallelGroup(Alignment.LEADING, false)
        				.addGroup(layout.createSequentialGroup()
        					.addGroup(layout.createParallelGroup(Alignment.LEADING)
        						.addGroup(layout.createSequentialGroup()
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addGroup(layout.createParallelGroup(Alignment.LEADING)
        								.addComponent(jPanel_5, GroupLayout.PREFERRED_SIZE, 393, GroupLayout.PREFERRED_SIZE)
        								.addGroup(layout.createSequentialGroup()
        									.addGap(105)
        									.addComponent(Query_button, GroupLayout.PREFERRED_SIZE, 180, GroupLayout.PREFERRED_SIZE))
        								.addGroup(layout.createSequentialGroup()
        									.addGap(54)
        									.addComponent(jPanel_1, GroupLayout.PREFERRED_SIZE, 288, GroupLayout.PREFERRED_SIZE))))
        						.addGroup(layout.createSequentialGroup()
        							.addGap(134)
        							.addComponent(quit_app, GroupLayout.PREFERRED_SIZE, 137, GroupLayout.PREFERRED_SIZE)))
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(bus_user_resultset, GroupLayout.PREFERRED_SIZE, 446, GroupLayout.PREFERRED_SIZE)
        					.addGap(74))
        				.addGroup(layout.createSequentialGroup()
        					.addGap(99)
        					.addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 220, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        					.addComponent(lblResults, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE)
        					.addGap(246))))
        );
        layout.setVerticalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(layout.createParallelGroup(Alignment.TRAILING)
        				.addComponent(lblResults)
        				.addGroup(layout.createParallelGroup(Alignment.LEADING)
        					.addComponent(business_radio)
        					.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        						.addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
        						.addComponent(jLabel2))))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(layout.createParallelGroup(Alignment.LEADING)
        				.addGroup(layout.createSequentialGroup()
        					.addGroup(layout.createParallelGroup(Alignment.TRAILING, false)
        						.addComponent(category, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        						.addComponent(attributes, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 329, Short.MAX_VALUE)
        						.addComponent(subcategory, Alignment.LEADING))
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(business_and, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        					.addComponent(jPanel4, GroupLayout.PREFERRED_SIZE, 277, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.RELATED))
        				.addGroup(layout.createSequentialGroup()
        					.addComponent(jPanel_1, GroupLayout.PREFERRED_SIZE, 357, GroupLayout.PREFERRED_SIZE)
        					.addGap(34)
        					.addComponent(jPanel_5, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        					.addGap(28)
        					.addComponent(Query_button, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.UNRELATED)
        					.addComponent(quit_app, GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE))
        				.addComponent(bus_user_resultset, GroupLayout.PREFERRED_SIZE, 636, GroupLayout.PREFERRED_SIZE))
        			.addContainerGap(300, GroupLayout.PREFERRED_SIZE))
        );
        getContentPane().setLayout(layout);

        Query_button.setVisible(true);

        pack();
    }                        
boolean user_query = false;

	
	// Execute query button action performed method
    @SuppressWarnings({ "rawtypes", "unchecked" })
	private void Query_buttonActionPerformed(java.awt.event.ActionEvent evt) {                                             
        
    	String business_and2 = business_and.getSelectedItem().toString(); 
        
    	// Check if business is selected
    	if(business_radio.isSelected()){
    		
    		// Query business data using the values provided in the GUI
	        if(category_list.size() > 0) {
	        	if(business_and2 == "OR") {
	        		
	        		// Query for 'OR' for business
		            str_cat="select business_id,business_name,city,state1,stars from business where business_id in(( select business_id from business_CATEGORY where (BUS_CATG_ID like ";
		            for(int i=0;i<category_list.size();i++){
		                if(i!=0)
		                    str_cat= str_cat + " or BUS_CATG_ID like ";
		                 str_cat= str_cat + "'%" + category_list.get(i).trim() + "%'";
		            }
		            str_cat=str_cat+" ) and (BUS_CATG_NAME like ";
		            for(int i=0;i<subcategory_list.size();i++)
		            {
		                if(i != 0)
		                    str_cat = str_cat + " or BUS_CATG_NAME like ";
		            
		                str_cat= str_cat + "'%" + subcategory_list.get(i).trim() + "%'";
		            }
		     
		            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		     
		            String from_date_review1 = sdf.format(fromreview_date.getDate());
		            String to_date_review1 = sdf.format(toreview_date.getDate());
		            String review_star1 = reviewstar_count.getSelectedItem().toString(); 
		            Integer review_star_value1= Integer.parseInt(review_star_value.getText());
		            String review_votes1 = reviewno_votes.getSelectedItem().toString();
		            Integer review_votes_value1=Integer.parseInt(review_star_value.getText());
		        
		            str_cat = str_cat + "))intersect (select distinct business_id from reviews where PUBLISH_DATE>= to_date('"  
		                 + from_date_review1 + "','YYYY-MM-DD') and PUBLISH_DATE <= to_date('" + to_date_review1
		                 + "','YYYY-MM-DD') group by business_id having sum(total_votes)" 
		                + review_votes1 + review_votes_value1+ " OR avg(stars)" + review_star1 + review_star_value1 + 
		                ") intersect (select distinct business_id from BUSINESSATTRIBUTES where BUS_ATTRIBUTE like ";
		                for(int i=category_list.size();i<attribute_list.size();i++)
		                {
		                    if(i != category_list.size())
		                        str_cat = str_cat + " or BUS_ATTRIBUTE like ";
		                
		                    str_cat= str_cat + "'%" + attribute_list.get(i).trim() + "%'";
		                } 
		            str_cat = str_cat + "))";
		         
		
		            
	        	}
	        	
	        	
	        	else{
	        		
	        		// Query for 'AND' for business
	        		str_cat="select business_id,business_name,city,state1,stars from business where business_id in(( select business_id from business_CATEGORY where (BUS_CATG_ID like ";
		            for(int i=0;i<category_list.size();i++){
		                if(i!=0)
		                    str_cat= str_cat + " or BUS_CATG_ID like ";
		                 str_cat= str_cat + "'%" + category_list.get(i).trim() + "%'";
		            }
		            str_cat=str_cat+" ) and (BUS_CATG_NAME like ";
		            for(int i=0;i<subcategory_list.size();i++)
		            {
		                if(i != 0)
		                    str_cat = str_cat + " or BUS_CATG_NAME like ";
		            
		                str_cat= str_cat + "'%" + subcategory_list.get(i).trim() + "%'";
		            }
		     
		            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		     
		            String from_date_review1 = sdf.format(fromreview_date.getDate());
		            String to_date_review1 = sdf.format(toreview_date.getDate());
		            String review_star1 = reviewstar_count.getSelectedItem().toString(); 
		            Integer review_star_value1= Integer.parseInt(review_star_value.getText());
		            String review_votes1 = reviewno_votes.getSelectedItem().toString();
		            Integer review_votes_value1=Integer.parseInt(review_star_value.getText());
		        
		            str_cat = str_cat + "))intersect (select distinct business_id from reviews where PUBLISH_DATE>= to_date('"  
		                 + from_date_review1 + "','YYYY-MM-DD') and PUBLISH_DATE <= to_date('" + to_date_review1
		                 + "','YYYY-MM-DD') group by business_id having sum(total_votes)" 
		                + review_votes1 + review_votes_value1+ " and avg(stars)" + review_star1 + review_star_value1 + 
		                ") intersect (select distinct business_id from BUSINESSATTRIBUTES where BUS_ATTRIBUTE like ";
		                for(int i=category_list.size();i<attribute_list.size();i++)
		                {
		                    if(i != category_list.size())
		                        str_cat = str_cat + " and BUS_ATTRIBUTE like ";
		                
		                    str_cat= str_cat + "'%" + attribute_list.get(i).trim() + "%'";
		                } 
		            str_cat = str_cat + "))";
	        		
	        	}
	        	System.out.println(str_cat);
	            query_display.setText(str_cat);
	            user_query = false;
	        }
        }
    	
    	// User is selected
        else{
        	
        	// Query for yelp user data from the values provided in the GUI
            user_query = true;
            String user_count2 = userreview_count.getSelectedItem().toString(); 
            Integer user_count_value1= Integer.parseInt(user_count_value.getText());
            String user_friend2 = userfriend_list.getSelectedItem().toString(); 
            Integer user_friend_value1= Integer.parseInt(user_friend_value.getText());
            String user_star2 = useravg_star.getSelectedItem().toString(); 
            Integer user_star_value1= Integer.parseInt(user_star_value.getText());
            String user_and2 = user_and.getSelectedItem().toString(); 
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String no_votes2 = userno_votes.getSelectedItem().toString(); 
            Integer no_votes_value1 = Integer.parseInt(textField.getText());
     
            String yelp_since = sdf.format(yelpuser_date.getDate());
            
            // Query for 'AND' for yelp user
            if(user_and2 == "AND") {
                str_cat="select first_name,yelping_since,average_stars,total_votes,yelp_id,friend_list from yelp_user where yelp_id in((select yelp_id from yelp_user "
                 +"where (FRIEND_LIST)"+user_friend2+user_friend_value1+") INTERSECT (select yelp_id from "+
                 "yelp_user where average_stars"+user_star2+user_star_value1+" "+user_and2+" review_count "+
                 user_count2 +user_count_value1+" AND total_votes "+no_votes2+no_votes_value1+")) and yelping_since >= to_date('" + yelp_since
                 + "','YYYY-MM-DD')";
            } 
            
         // Query for 'OR' for yelp user
            else {
                str_cat="select first_name,yelping_since,average_stars,total_votes,yelp_id,friend_list from yelp_user where yelp_id in((select yelp_id from yelp_user "
                 +"where (FRIEND_LIST)"+user_friend2+user_friend_value1+") UNION (select yelp_id from "+
                 "yelp_user where average_stars"+user_star2+user_star_value1+" "+user_and2+" review_count "+
                 user_count2 +user_count_value1+" OR total_votes "+no_votes2+no_votes_value1+")) OR yelping_since >= to_date('" + yelp_since
                 + "','YYYY-MM-DD')";
            }
   
            System.out.println(str_cat);
            query_display.setText(str_cat);
        }
       
       
        rs1=jdbc_connectivity(str_cat);
        
        JTable jtable = new JTable();
        jtable.setLayout(new GridLayout());
        
        // Display resultset
        bus_user_resultset.setPreferredSize(new Dimension(2000,250));
        jtable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
       
        try {
                rsmetadata=rs1.getMetaData();
               columns=rsmetadata.getColumnCount();
            
               DefaultTableModel dtm=new DefaultTableModel();
               Vector columns_name=new Vector();
               Vector data_rows=new Vector();
               for(int i=1;i<=columns;i++)
               {
                     columns_name.addElement(rsmetadata.getColumnName(i));
               }
               dtm.setColumnIdentifiers(columns_name);
               while(rs.next())
               {
                   data_rows=new Vector();
                   for(int j=1;j<=columns;j++)
                   {
                       data_rows.addElement(rs.getString(j));
                       
                   }
                   dtm.addRow(data_rows);
               }
               jtable.setModel(dtm);
            
               for (int i=0; i<columns;i++) {
                   TableColumn column_obj = jtable.getColumnModel().getColumn(0);
                   column_obj.setMinWidth(150);
                   
               }
               bus_user_resultset.setViewportView(jtable);
               jtable.addMouseListener(new java.awt.event.MouseAdapter()
               {
            	   
            	// Get reviews
                public void mouseClicked(java.awt.event.MouseEvent e)
                    {
                    int row=jtable.rowAtPoint(e.getPoint());
                    int col= jtable.columnAtPoint(e.getPoint());
                    
                    if(user_query == false) {
                    	
                    	// Get reviews for a particular business for every user who has reviewed it
                        if(col==0)
                            value= jtable.getValueAt(row,col).toString();
                        System.out.println(value);
                            review_query="Select y.first_name,r.text1,r.text2 from reviews r, business b, yelp_user y where b.business_id" +
                                "=r.business_id and r.user_id=y.yelp_id and b.business_id='"+value+"'";
                    	} 
                    // Get reviews for a particular user who has reviewed for any businesses
                    else {
                       if(col==4)
                            value= jtable.getValueAt(row,col).toString();
                       System.out.println(value);
                       review_query ="Select y.first_name,r.text1,r.text2 from reviews r, yelp_user y where r.user_id=y.yelp_id and r.user_id='"+value+"'";
                       System.out.println(review_query);      
                    }
                    rs=jdbc_connectivity(review_query);
                    String review_str="";
                    try {
                        while(rs.next())
                        {  
                        	
                           review_str=review_str+"User: "+rs.getString(1)+"\n"+"Reviews: "+rs.getString(2)+"\n"+rs.getString(3)+"\n---------------------\n";
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(hw3.class.getName()).log(Level.SEVERE, null, ex);
                    }
                  
                    TextFrame textFrame = new TextFrame(review_str);
                    textFrame.setVisible(true);

                     }
                }
               );
            }
      catch (SQLException ex) {
                Logger.getLogger(hw3.class.getName()).log(Level.SEVERE, null, ex);
            }
      
       
        
    }                                            

    private void review_starActionPerformed(java.awt.event.ActionEvent evt) {                                            
        // TODO add your handling code here:
    }                                           

    private void review_star_valueActionPerformed(java.awt.event.ActionEvent evt) {                                                  
        // TODO add your handling code here:
    }                                                 

    private void review_votesActionPerformed(java.awt.event.ActionEvent evt) {                                             
        // TODO add your handling code here:
    }                                            

    private void user_countActionPerformed(java.awt.event.ActionEvent evt) {                                           
        // TODO add your handling code here:
    }                                          

    private void user_friendActionPerformed(java.awt.event.ActionEvent evt) {                                            
        // TODO add your handling code here:
    }                                           

    private void user_andActionPerformed(java.awt.event.ActionEvent evt) {                                         
        // TODO add your handling code here:
    }                                        

    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        // The Nimbus look and feel
        
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(hw3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(hw3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(hw3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(hw3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        

        System.out.println("Starting yelp application");
       
       
        // run
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new hw3().setVisible(true);
           
            }
        });
    }

                    
    public static javax.swing.JButton Query_button;
    public static javax.swing.JScrollPane category;
    private com.toedter.calendar.JDateChooser fromreview_date;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel_1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel_5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea query_display;
    private javax.swing.JScrollPane bus_user_resultset;
    private javax.swing.JComboBox<String> reviewstar_count;
    private javax.swing.JTextField review_star_value;
    private javax.swing.JComboBox<String> reviewno_votes;
    private javax.swing.JTextField review_votes_value;
    public static javax.swing.JScrollPane subcategory;
    private com.toedter.calendar.JDateChooser toreview_date;
    private javax.swing.JComboBox<String> user_and;
    private javax.swing.JComboBox<String> userreview_count;
    private javax.swing.JTextField user_count_value;
    private javax.swing.JComboBox<String> userfriend_list;
    private javax.swing.JTextField user_friend_value;
    private javax.swing.JComboBox<String> useravg_star;
    JComboBox<String> no_votes = new JComboBox<String>();
    private JComboBox<String> userno_votes;
    
    private javax.swing.JTextField user_star_value;
    private com.toedter.calendar.JDateChooser yelpuser_date;
    private JTextField textField;
    private JComboBox<String> business_and;
    private JLabel lblResults;
    private JRadioButton business_radio;
    private JRadioButton user_radio;
}

@SuppressWarnings("serial")
class TextFrame extends JFrame
{
   public TextFrame(String content) {
      super("TextFrame");
  
      JTextArea textarea = new JTextArea();
      
      textarea.setText(content);
      getContentPane().add(textarea);
     
      addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent we) {
            dispose();
         }
      });
       
      setSize(2000, 1000);
   }
}