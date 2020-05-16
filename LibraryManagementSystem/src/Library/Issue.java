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

public class Issue extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	Connection con;
	PreparedStatement pat;
	ResultSet rs;
	private JTable table;

	JComboBox txtBook = new JComboBox();
	JTextPane txtName = new JTextPane();
	JDateChooser txtBd = new JDateChooser();

	
	
	JDateChooser txtRd = new JDateChooser();
	Issue a = this;
	private JTextField txtId;
	
	
	public void connect() {
		
		try{
			Class.forName("com.mysql.cj.jdbc.Driver");
			con= DriverManager.getConnection("jdbc:mysql://localhost:3306/LMS","root","1234");
		    
		}
		catch(SQLException e) {
			Logger.getLogger(Issue.class.getName()).log(Level.SEVERE,null,e);
		}
		catch(ClassNotFoundException e) {
			Logger.getLogger(Issue.class.getName()).log(Level.SEVERE,null,e);
		}
		
	}
	
	
	public class BookItem{
		int id;
		String name;
		public BookItem(int id,String name){
			this.id=id;
			this.name=name;
		}
		public String toString() {
			return name;
		}
	}
	
	public void book() {
		try {
			pat= con.prepareStatement("select * from book");
			rs=pat.executeQuery();
			txtBook.removeAllItems();
			
			while(rs.next()) {
				txtBook.addItem(new BookItem(rs.getInt(1),rs.getString(2)));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
		public void issueLoad() {
		int c;
		try {
			pat=con.prepareStatement("select i.id,m.mem_id,b.name,i.issueDate,i.returnDate from issue i join mem m on i.mem_id=m.mem_id join book b on i.book=b.Book_id ");
			rs=pat.executeQuery();

			
			ResultSetMetaData rsd=rs.getMetaData();
			c=rsd.getColumnCount();
			DefaultTableModel d=(DefaultTableModel)table.getModel();
			
			d.setRowCount(0);
			
			while(rs.next()) {
				Vector<String> v= new Vector<String>();
				for(int i=1;i<=c;i++) {
					v.add(rs.getString("i.id"));
					v.add(rs.getString("m.mem_id"));
					v.add(rs.getString("b.name"));
					v.add(rs.getString("i.issueDate"));
					v.add(rs.getString("i.returnDate"));
				}
				d.addRow(v);
			}
		}
		catch(SQLException e) {
			Logger.getLogger(Issue.class.getName()).log(Level.SEVERE,null,e);
		}
	}

	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Issue frame = new Issue();
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
	public Issue() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 784, 492);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(204, 102, 204));
		contentPane.add(panel, BorderLayout.SOUTH);
		
		
		JLabel lblAuthor = new JLabel("Issue Book");
		lblAuthor.setForeground(Color.WHITE);
		lblAuthor.setFont(new Font("Times New Roman", Font.BOLD, 18));
		
		JLabel lblAuthorName = new JLabel("Member Id");
		lblAuthorName.setForeground(new Color(255, 255, 204));
		lblAuthorName.setFont(new Font("Times New Roman", Font.BOLD, 14));
		
		JButton btnNewButton = new JButton("Add");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				//DefaultTableModel d=(DefaultTableModel)table.getModel();

				String mid=txtId.getText().toString();
				BookItem b=(BookItem)txtBook.getSelectedItem();
				SimpleDateFormat date=new SimpleDateFormat("yyyy-MM-dd");
				String idate=date.format(txtBd.getDate()).toString();
				String rdate=date.format(txtRd.getDate()).toString();
				
				
				try {
					pat=con.prepareStatement("insert into issue(mem_id,book,issueDate,returnDate)values(?,?,?,?)");
					//pat.setString(1,name.substring(0,5)+"001");
					pat.setString(1,mid);
					pat.setInt(2,b.id);
					pat.setString(3,idate);
					pat.setString(4,rdate);
					
					int k= pat.executeUpdate();
					
					if(k==1) {
						JOptionPane.showMessageDialog(a, "Row Inserted Successfully!!");
						txtId.setText("");
						txtBook.setSelectedIndex(-1);
						txtName.setText("");
						txtId.requestFocus();
						
						issueLoad();
					}
					else {
						JOptionPane.showMessageDialog(a,"Error!!");
					}
					
				}
				catch(SQLException e)
				{
					Logger.getLogger(Issue.class.getName()).log(Level.SEVERE,null,e);
			
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
				BookItem bi = (BookItem)txtBook.getSelectedItem();
				SimpleDateFormat date=new SimpleDateFormat("yyyy-mm-dd");
				String idate=date.format(txtBd.getDate()).toString();
				String rdate=date.format(txtRd.getDate()).toString();
				
				try {
					pat=con.prepareStatement("update issue set mem_id=?,book=?,issueDate=?,returnDate=? where id=?");
					//pat.setString(1,name.substring(0,5)+"001");
					pat.setString(1,mid);
					pat.setInt(2,bi.id);
					pat.setString(3,idate);
					pat.setString(4,rdate);
					pat.setInt(5, id);
					
					int k= pat.executeUpdate();
					
					if(k==1) {
						JOptionPane.showMessageDialog(a, "Row Updated Successfully!!");
						txtId.setText("");
						txtBook.setSelectedIndex(-1);
						txtId.requestFocus();
						issueLoad();
						btnNewButton.setEnabled(true);
					}
					else {
						JOptionPane.showMessageDialog(a,"Error!!");
					}
					
				}
				catch(SQLException ex)
				{
					Logger.getLogger(Issue.class.getName()).log(Level.SEVERE,null,ex);
			
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
					pat=con.prepareStatement("delete from issue where id=?");
					pat.setInt(1,id);
					
					int k= pat.executeUpdate();
					
					if(k==1) {
						JOptionPane.showMessageDialog(a, "Row deleted !!");
						txtId.setText("");
						txtBook.setSelectedIndex(-1);;
						txtId.requestFocus();
						issueLoad();
						btnNewButton.setEnabled(true);
					}
					else {
						JOptionPane.showMessageDialog(a,"Error!!");
					}
					
				}
				catch(SQLException ex)
				{
					Logger.getLogger(Issue.class.getName()).log(Level.SEVERE,null,ex);
			
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
		
		JLabel lblEdition = new JLabel("Date");
		lblEdition.setForeground(new Color(255, 255, 204));
		lblEdition.setFont(new Font("Times New Roman", Font.BOLD, 14));
		
		
		JLabel lblReturnDate = new JLabel("Return Date");
		lblReturnDate.setFont(new Font("Times New Roman", Font.BOLD, 14));
		lblReturnDate.setForeground(new Color(255, 255, 204));
		
		txtId = new JTextField();
		txtId.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if(arg0.getKeyCode()==KeyEvent.VK_ENTER) {
					String id=txtId.getText();
					try {
						pat=con.prepareStatement("select name from mem where mem_id=? ");
						pat.setString(1,id);
						rs=pat.executeQuery();
						if(rs.next()==false){
							JOptionPane.showMessageDialog(a,"Member Id Not Found");
						}
						else{
							String mname=rs.getString("name");
							txtName.setText(mname);
								
						}
					
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				
			}
		});
		
		txtId.setColumns(10);
		
		
		
		
		
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING, false)
							.addGroup(gl_panel.createSequentialGroup()
								.addGap(72)
								.addComponent(lblAuthor))
							.addGroup(gl_panel.createSequentialGroup()
								.addContainerGap()
								.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
									.addComponent(lblPublisher)
									.addComponent(lblEdition, GroupLayout.PREFERRED_SIZE, 58, GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(ComponentPlacement.RELATED, 55, Short.MAX_VALUE)
								.addGroup(gl_panel.createParallelGroup(Alignment.LEADING, false)
									.addComponent(txtBd, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(txtBook, 0, 205, Short.MAX_VALUE)))
							.addGroup(gl_panel.createSequentialGroup()
								.addContainerGap()
								.addComponent(lblReturnDate)
								.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(txtRd, GroupLayout.PREFERRED_SIZE, 205, GroupLayout.PREFERRED_SIZE))
							.addGroup(gl_panel.createSequentialGroup()
								.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
									.addGroup(gl_panel.createSequentialGroup()
										.addContainerGap()
										.addComponent(lblNewLabel)
										.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
									.addGroup(gl_panel.createSequentialGroup()
										.addContainerGap()
										.addComponent(lblAuthorName)
										.addGap(46)))
								.addGroup(gl_panel.createParallelGroup(Alignment.LEADING, false)
									.addComponent(txtName, GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
									.addComponent(txtId))))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(66)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING, false)
								.addGroup(gl_panel.createSequentialGroup()
									.addComponent(btnDelete)
									.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(btnCancel))
								.addGroup(gl_panel.createSequentialGroup()
									.addComponent(btnNewButton)
									.addGap(54)
									.addComponent(btnNewButton_1)))))
					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 418, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addContainerGap()
							.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 407, Short.MAX_VALUE))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(25)
							.addComponent(lblAuthor)
							.addGap(39)
							.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblAuthorName)
								.addComponent(txtId, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(21)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(txtName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblNewLabel))
							.addGap(15)
							.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblPublisher)
								.addComponent(txtBook, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(15)
							.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
								.addComponent(lblEdition, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
								.addComponent(txtBd, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(18)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblReturnDate)
								.addComponent(txtRd, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED, 71, Short.MAX_VALUE)
							.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnNewButton)
								.addComponent(btnNewButton_1))
							.addGap(18)
							.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnDelete)
								.addComponent(btnCancel))
							.addGap(28)))
					.addGap(38))
		);
		txtBd.setDateFormatString("yyyy-MM-dd");
		txtRd.setDateFormatString("yyyy-MM-dd");
		
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
				txtBook.setSelectedItem(d.getValueAt(si, 2).toString());
				java.util.Date date;
				try {
					date = new SimpleDateFormat("yyyy-MM-dd").parse(d.getValueAt(si,3).toString());

					txtBd.setDate(date);
					date = new SimpleDateFormat("yyyy-MM-dd").parse(d.getValueAt(si,4).toString());
					txtRd.setDate(date);
					
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				btnNewButton.setEnabled(false); 
				
				
			}
		});
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Id", "Member Id", "Book", "Borrow Date", "Return Date"
			}
		) {
			Class[] columnTypes = new Class[] {
				Integer.class, String.class, String.class, String.class, String.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			boolean[] columnEditables = new boolean[] {
				false, true, true, true, true
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		scrollPane.setViewportView(table);
		panel.setLayout(gl_panel);
		//add(new JScrollPane(table));
        connect();
        issueLoad();
        book();
	}
}
