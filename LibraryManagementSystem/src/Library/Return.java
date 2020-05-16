package Library;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.Font;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JTextPane;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import com.toedter.calendar.JDateChooser;

import Library.Book.AuthorItem;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JTextField;

public class Return extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	Connection con;
	PreparedStatement pat;
	ResultSet rs;
	private JTable table;
	Return a = this;
	private JTextField txtEl;
	private JTextField txtF;
	private JTextField txtId;
	private JLabel txtMem;
	private JLabel txtBook;

	JLabel txtRd = new JLabel("New label");
	
	public void connect() {
		
		try{
			Class.forName("com.mysql.cj.jdbc.Driver");
			con= DriverManager.getConnection("jdbc:mysql://localhost:3306/LMS","root","1234");
		    
		}
		catch(SQLException e) {
			Logger.getLogger(Return.class.getName()).log(Level.SEVERE,null,e);
		}
		catch(ClassNotFoundException e) {
			Logger.getLogger(Return.class.getName()).log(Level.SEVERE,null,e);
		}
		
	}
	
		public void returnLoad() {
		int c;
		try {
			pat=con.prepareStatement("select * from breturn ");
			rs=pat.executeQuery();

			
			ResultSetMetaData rsd=rs.getMetaData();
			c=rsd.getColumnCount();
			DefaultTableModel d=(DefaultTableModel)table.getModel();
			
			d.setRowCount(0);
			
			while(rs.next()) {
				Vector<String> v= new Vector<String>();
				for(int i=1;i<=c;i++) {
					v.add(rs.getString("id"));
					v.add(rs.getString("mid"));
					v.add(rs.getString("mname"));
					v.add(rs.getString("bname"));
					v.add(rs.getString("rdate"));
					v.add(rs.getString("elap"));
					v.add(rs.getString("fine"));
				}
				d.addRow(v);
			}
		}
		catch(SQLException e) {
			Logger.getLogger(Return.class.getName()).log(Level.SEVERE,null,e);
		}
	}

	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Return frame = new Return();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Return() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 873, 492);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(204, 102, 204));
		contentPane.add(panel, BorderLayout.CENTER);
		
		
		JLabel lblAuthor = new JLabel("Return Book");
		lblAuthor.setForeground(Color.WHITE);
		lblAuthor.setFont(new Font("Times New Roman", Font.BOLD, 18));
		
		JLabel lblAuthorName = new JLabel("Member Id");
		lblAuthorName.setForeground(new Color(255, 255, 204));
		lblAuthorName.setFont(new Font("Times New Roman", Font.BOLD, 14));
		
		JButton btnNewButton = new JButton("Add");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {


				String mid=txtId.getText();
				String mname=txtMem.getText();
				String bname=txtBook.getText();
				String rd=txtRd.getText();
				String el=txtEl.getText();
				String f=txtF.getText();
				
				
				
				try {
					pat=con.prepareStatement("insert into breturn(mid,mname,bname,rdate,elap,fine)values(?,?,?,?,?,?)");
					//pat.setString(1,name.substring(0,5)+"001");
					pat.setString(1,mid);
					pat.setString(2,mname);
					pat.setString(3,bname);
					pat.setString(4,rd);
					pat.setString(5,el);
					pat.setString(6,f);
					int k= pat.executeUpdate();
					
					pat=con.prepareStatement("delete from issue where mem_id=?");
					pat.setString(1,mid);
					pat.executeUpdate();
					
					
					if(k==1) {
						JOptionPane.showMessageDialog(a, "Book Returned Successfully!!");
						txtId.setText("");
						txtMem.setText("");
						txtBook.setText("");
						txtRd.setText("");
						txtEl.setText("");
						txtF.setText("");
						
						txtId.requestFocus();
						
						returnLoad();
					}
					else {
						JOptionPane.showMessageDialog(a,"Error!!");
					}
					
				}
				catch(SQLException e)
				{
					Logger.getLogger(Return.class.getName()).log(Level.SEVERE,null,e);
			
				}
				
			}
		});
		
		JButton btnNewButton_1 = new JButton("Update");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel d=(DefaultTableModel)table.getModel();
				//btnNewButton.setEnabled(false);
				int sI=table.getSelectedRow();
				
				int id=Integer.parseInt(d.getValueAt(sI, 0).toString());
				
				
				String mid=txtId.getText();
				String mname=txtMem.getText();
				String bname=txtBook.getText();
				String rd=txtRd.getText();
				String el=txtEl.getText();
				String f=txtF.getText();
	
				
				try {
					pat=con.prepareStatement("update breturn set mid=?,mname=?,bname=?,rdate=?,elap=?,fine=? where id=?");
					//pat.setString(1,name.substring(0,5)+"001");
					pat.setString(1,mid);
					pat.setString(2,mname);
					pat.setString(3,bname);
					pat.setString(4,rd);
					pat.setString(5, el);
					pat.setString(6, f);
					pat.setInt(7,id);
					
					int k= pat.executeUpdate();
					
					if(k==1) {
						JOptionPane.showMessageDialog(a, "Row Updated Successfully!!");
						txtId.setText("");
						txtMem.setText("");
						txtBook.setText("");
						txtRd.setText("");
						txtEl.setText("");
						txtF.setText("");
						
						txtId.requestFocus();
						
						returnLoad();
						btnNewButton.setEnabled(true);
					}
					else {
						JOptionPane.showMessageDialog(a,"Error!!");
					}
					
				}
				catch(SQLException ex)
				{
					Logger.getLogger(Return.class.getName()).log(Level.SEVERE,null,ex);
			
				}
				
			}
		});
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel d=(DefaultTableModel)table.getModel();
				int sI=table.getSelectedRow();
				
				int id=Integer.parseInt(d.getValueAt(sI, 0).toString());
				
				
				
				try {
					pat=con.prepareStatement("delete from breturn where id=?");
					pat.setInt(1,id);
					
					int k= pat.executeUpdate();
					
					if(k==1) {
						JOptionPane.showMessageDialog(a, "Row deleted !!");
						txtId.setText("");
						txtMem.setText("");
						txtBook.setText("");
						txtRd.setText("");
						txtEl.setText("");
						txtF.setText("");
						
						
						txtId.requestFocus();
						returnLoad();
						btnNewButton.setEnabled(true);
					}
					else {
						JOptionPane.showMessageDialog(a,"Error!!");
					}
					
				}
				catch(SQLException ex)
				{
					Logger.getLogger(Return.class.getName()).log(Level.SEVERE,null,ex);
			
				}
				
		
			}
		});
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				a.setVisible(false);
			}
		});
		
		
		JLabel lblNewLabel = new JLabel("Member Name");
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 14));
		lblNewLabel.setForeground(new Color(255, 255, 204));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
			}
		});
		
		JLabel lblPublisher = new JLabel("Book");
		lblPublisher.setForeground(new Color(255, 255, 204));
		lblPublisher.setFont(new Font("Times New Roman", Font.BOLD, 14));
		
		
		JLabel lblReturnDate = new JLabel("Return Date");
		lblReturnDate.setFont(new Font("Times New Roman", Font.BOLD, 14));
		lblReturnDate.setForeground(new Color(255, 255, 204));
		
		txtMem = new JLabel("New label");
		txtMem.setFont(new Font("Times New Roman", Font.BOLD, 14));
		txtMem.setForeground(new Color(255, 204, 255));
		
		txtBook = new JLabel("New label");
		txtBook.setForeground(new Color(255, 204, 255));
		txtBook.setFont(new Font("Times New Roman", Font.BOLD, 14));
		
		JLabel lblDaysElap = new JLabel("Days Elap");
		lblDaysElap.setForeground(new Color(255, 255, 204));
		lblDaysElap.setFont(new Font("Times New Roman", Font.BOLD, 14));
		
		JLabel lblFine = new JLabel("Fine");
		lblFine.setForeground(new Color(255, 255, 204));
		lblFine.setFont(new Font("Times New Roman", Font.BOLD, 14));
		
		txtEl = new JTextField();
		txtEl.setColumns(10);
		
		txtF = new JTextField();
		txtF.setColumns(10);
		
		txtId = new JTextField();
		txtId.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if(arg0.getKeyCode()==KeyEvent.VK_ENTER) {
				String id=txtId.getText();
				try {
					pat=con.prepareStatement("select m.name, b.name,i.returnDate,DATEDIFF(now(),i.returnDate) as elap from issue i join mem m on i.mem_id=m.mem_id join book b on i.book=b.Book_id and i.mem_id=?");
					pat.setString(1,id);
					rs=pat.executeQuery();
					if(rs.next()==false){
						JOptionPane.showMessageDialog(a,"Member Id Not Found");
					}
					else{
						String mname=rs.getString("m.name");
						String bname=rs.getString("b.name");
						
						txtMem.setText(mname.trim());
						txtBook.setText(bname.trim());
						
						String l=rs.getString("i.returnDate");
						txtRd.setText(l.trim());
						
						String elp= rs.getString("elap");
						int e= Integer.parseInt(elp);
						if(e>0) {
							txtEl.setText(elp);
							int fine=e*5;
							txtF.setText(String.valueOf(fine));
						}
						else
						{
							txtEl.setText("0");
							txtF.setText("0");
						}
						
					}
				
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
			}
		});
		txtId.setColumns(10);
		
		txtRd.setForeground(new Color(255, 204, 255));
		txtRd.setFont(new Font("Times New Roman", Font.BOLD, 14));
		
		
		
		
		
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel.createSequentialGroup()
									.addContainerGap()
									.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
										.addComponent(lblNewLabel)
										.addComponent(lblPublisher)
										.addComponent(lblReturnDate)
										.addComponent(lblDaysElap, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE)
										.addComponent(lblFine, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE)
										.addComponent(lblAuthorName)))
								.addGroup(gl_panel.createSequentialGroup()
									.addGap(60)
									.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
										.addComponent(btnDelete)
										.addComponent(btnNewButton))))
							.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING, false)
								.addGroup(Alignment.LEADING, gl_panel.createSequentialGroup()
									.addGap(26)
									.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
										.addComponent(btnCancel)
										.addComponent(btnNewButton_1)))
								.addGroup(Alignment.LEADING, gl_panel.createSequentialGroup()
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
										.addComponent(txtF)
										.addComponent(txtEl)
										.addComponent(txtRd)
										.addComponent(txtId, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(txtMem)
										.addComponent(txtBook, GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)))))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(48)
							.addComponent(lblAuthor)))
					.addGap(83)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 538, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(103)
					.addComponent(lblAuthorName)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(lblPublisher)
					.addGap(18)
					.addComponent(lblReturnDate)
					.addGap(18)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblDaysElap, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtEl, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblFine, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtF, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(150, Short.MAX_VALUE))
				.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
					.addGap(22)
					.addComponent(lblAuthor)
					.addGap(52)
					.addComponent(txtId, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(txtMem)
					.addGap(18)
					.addComponent(txtBook, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(txtRd, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 128, Short.MAX_VALUE)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnNewButton)
						.addComponent(btnNewButton_1))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnDelete)
						.addComponent(btnCancel))
					.addGap(38))
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		
		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				DefaultTableModel d=(DefaultTableModel)table.getModel();
				int si= table.getSelectedRow();
				//int x=d.getValueAt(si, 3)
				//AuthorItem ai=(AuthorItem)txtAuthor.getSelectedItem();
				//PublisherItem pi=(PublisherItem)txtPub.getSelectedItem();
				int id=Integer.parseInt(d.getValueAt(si, 0).toString());
				txtId.setText(d.getValueAt(si,1).toString());
				txtMem.setText(d.getValueAt(si,2).toString());
				txtBook.setText(d.getValueAt(si,3).toString());
				txtRd.setText(d.getValueAt(si,4).toString());
				txtEl.setText(d.getValueAt(si,5).toString());
				txtF.setText(d.getValueAt(si,6).toString());
								
				btnNewButton.setEnabled(false); 
				
				
			}
		});
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Id", "Mem_Id", "Member Name", "Book", "Return Date", "Days Elapsed", "Fine"
			}
		) {
			Class[] columnTypes = new Class[] {
				Integer.class, Integer.class, String.class, String.class, String.class, String.class, String.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			boolean[] columnEditables = new boolean[] {
				true, true, false, false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table.getColumnModel().getColumn(0).setMinWidth(8);
		scrollPane.setViewportView(table);
		panel.setLayout(gl_panel);
		//add(new JScrollPane(table));
        connect();
        returnLoad();
       
	}
}
