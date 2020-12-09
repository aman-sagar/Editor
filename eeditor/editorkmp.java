package eeditor;

import java.awt.Color;
import javax.swing.*; 
import java.io.*;
import java.awt.event.*;
import java.util.*;
import findAndReplace.*;

import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;


public class editorkmp extends JFrame implements ActionListener {

	JTextPane t;
	JScrollPane scrollpane;
	JFrame f;
	JMenuBar mb;

	public editorkmp() {

		f = new JFrame("TextEditor");

		try {

			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } 
        catch (Exception e) { 
        } 
		      
		t = new JTextPane();
		scrollpane = new JScrollPane(t);
		
		SimpleAttributeSet attributeSet = new SimpleAttributeSet();
		StyleConstants.setFontSize(attributeSet, 16);
		StyleConstants.setForeground(attributeSet, Color.DARK_GRAY);
		t.setCharacterAttributes(attributeSet, true);


		mb = new JMenuBar(); 
		
		UIManager.put("MenuBar.background", Color.orange);
		UIManager.put("MenuItem.selectionBackground", Color.red);
		UIManager.put("MenuItem.background", Color.orange);
		//File menu
		JMenu m1 = new JMenu("File"); 
		 
		JMenuItem mi1 = new JMenuItem("New"); 
		JMenuItem mi2 = new JMenuItem("Open"); 
		JMenuItem mi3 = new JMenuItem("Save");
		 		 
		mi1.addActionListener(this); 
		mi2.addActionListener(this); 
		mi3.addActionListener(this); 
		 
		m1.add(mi1); 
		m1.add(mi2); 
		m1.add(mi3); 
		
		
		//Edit-menu 
		JMenu m2 = new JMenu("Edit"); 
		 
		JMenuItem mi4 = new JMenuItem("cut"); 
		JMenuItem mi5 = new JMenuItem("copy"); 
		JMenuItem mi6 = new JMenuItem("paste");
		JMenuItem mif = new JMenuItem("Find");
		JMenuItem mir = new JMenuItem("Replace");
		 
		mi4.addActionListener(this); 
		mi5.addActionListener(this); 
		mi6.addActionListener(this);
		mif.addActionListener(this);
		mir.addActionListener(this);

		m2.add(mi4); 
		m2.add(mi5); 
		m2.add(mi6);
		m2.add(mif);
		m2.add(mir);
		
		//Mode-menu
		JMenu mode = new JMenu("Mode");

		JMenuItem DE = new JMenuItem("darkEditor");
		JMenuItem LE = new JMenuItem("lightEditor");
		DE.addActionListener(this);
		LE.addActionListener(this); 

		mode.add(DE);
		mode.add(LE);

		mb.add(m1); 
		mb.add(m2); 
		mb.add(mode); 

		f.setJMenuBar(mb); 
		f.add(scrollpane); 
		f.setSize(1000, 1000);
		f.setResizable(true);
		f.setVisible(true); 
	} 

	ArrayList<Integer> indexes = new ArrayList<>();
	int p;
	String find;
	
	// If a button is pressed 
	public void actionPerformed(ActionEvent e) 
	{ 
		String s = e.getActionCommand(); 

		if (s.equals("cut")) { 
			t.cut(); 
		} 
		else if (s.equals("copy")) { 
			t.copy(); 
		} 
		else if (s.equals("paste")) { 
			t.paste(); 
		} 
		else if (s.equals("Save")) { 
			 
			JFileChooser j = new JFileChooser("f:"); 
	
			int r = j.showSaveDialog(null); 

			if (r == JFileChooser.APPROVE_OPTION) { 

				
				File fi = new File(j.getSelectedFile().getAbsolutePath()); 

				try { 
					 
					FileWriter wr = new FileWriter(fi, false); 

					 
					BufferedWriter w = new BufferedWriter(wr); 

					 
					w.write(t.getText()); 

					w.flush(); 
					w.close(); 
				} 
				catch (Exception evt) { 
					JOptionPane.showMessageDialog(f, evt.getMessage()); 
				} 
			} 
			// If the user cancelled the operation 
			else
				JOptionPane.showMessageDialog(f, "the user cancelled the operation"); 
		} 
		
		else if (s.equals("Open")) { 
			// Create an object of JFileChooser class 
			JFileChooser j = new JFileChooser("f:"); 

			// Invoke the showsOpenDialog function to show the save dialog 
			int r = j.showOpenDialog(null); 

			// If the user selects a file 
			if (r == JFileChooser.APPROVE_OPTION) { 
				 
				File fi = new File(j.getSelectedFile().getAbsolutePath()); 

				try { 
					 
					String s1 = "", sl = "";  
					 
					FileReader fr = new FileReader(fi); 
 
					BufferedReader br = new BufferedReader(fr); 

					sl = br.readLine(); 

					// Take the input from the file 
					while ((s1 = br.readLine()) != null) { 
						sl = sl + "\n" + s1; 
					} 

					 
					t.setText(sl); 
				} 
				catch (Exception evt) { 
					JOptionPane.showMessageDialog(f, evt.getMessage()); 
				} 
			} 
			// If the user cancelled the operation 
			else
				JOptionPane.showMessageDialog(f, "Cancelled"); 
		} 
		else if (s.equals("New")) { 
			t.setText(""); 
		} 
		else if (s.equals("Find")) {
			
			find = JOptionPane.showInputDialog(null,
							"Find",
							JOptionPane.QUESTION_MESSAGE);
			int l = find.length();
							
			// try{Thread.sleep(550);}catch(Exception ee){}
			
			String text = t.getText();

			indexes = KMP.KMPSearch(find, text);
			p = indexes.size();
			
			Highlighter h = t.getHighlighter();
			Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.pink);

			try{
				for(int i=0; i<p; i++) {
					h.addHighlight(indexes.get(i), indexes.get(i)+l, painter);
				}
			}catch(Exception ex) {
				System.out.println(ex);
			}
			
			
			// t.setText(text);
							
		}
		else if(s.equals("Replace")){
			String replacewith = JOptionPane.showInputDialog(null,
							"Replace",
							JOptionPane.QUESTION_MESSAGE);
			
			String text = t.getText();
			int l = find.length();

			for (int i = 0; i < p; i++) {
				String substring1 = text.substring(0, indexes.get(i));
				String substring2 = text.substring(indexes.get(i)+l, text.length());
				text = substring1 + replacewith + substring2;
						
				for (int j = i + 1; j < p; j++) {
					int changedValue = indexes.get(j) - l + replacewith.length();
					indexes.set(j, changedValue);
				}
			}
			t.setText(text);
		}
		else if (s.equals("darkEditor")) {
			
			String text = t.getText();
			
			t.setBackground(Color.DARK_GRAY);

			SimpleAttributeSet attributeSet = new SimpleAttributeSet();
			StyleConstants.setFontSize(attributeSet, 16);
			StyleConstants.setForeground(attributeSet, Color.white);
			t.setCharacterAttributes(attributeSet, true);
			t.setCaretColor(Color.RED);

			t.setText(text);
		}
		else if (s.equals("lightEditor")) {
			
			String text = t.getText();
			
			t.setBackground(Color.WHITE);

			SimpleAttributeSet attributeSet = new SimpleAttributeSet();
			StyleConstants.setFontSize(attributeSet, 16);
			StyleConstants.setForeground(attributeSet, Color.DARK_GRAY);
			t.setCharacterAttributes(attributeSet, true);	
			t.setCaretColor(Color.BLACK);
			
			t.setText(text);
		}
	}  	 
} 
