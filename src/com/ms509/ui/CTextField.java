package com.ms509.ui;

import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.plaf.SliderUI;
import javax.swing.undo.UndoManager;

import com.ms509.util.ConstantClass;

/*
 * 在使用了非默认的LookAndFeel的情况下，Mac系统全选、复制、粘贴的修饰键由Ctrl改成Command
 * @author Chora
 */
public class CTextField extends JTextField {
	private UndoManager undo = new UndoManager();;

	public CTextField() {
		this.addKeyListener(new CKeyListener());
		this.getDocument().addUndoableEditListener(new UndoableEditListener() {
			public void undoableEditHappened(UndoableEditEvent e) {
				undo.addEdit(e.getEdit());
			}
		});
	}

	public CTextField(String name) {
		super(name);
		this.getDocument().addUndoableEditListener(new UndoableEditListener() {
			public void undoableEditHappened(UndoableEditEvent e) {
				undo.addEdit(e.getEdit());
			}
		});
		this.addKeyListener(new CKeyListener());
	}

	class CKeyListener extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			int event_ctrl_mask = Toolkit.getDefaultToolkit()
					.getMenuShortcutKeyMask();
			boolean isMac = event_ctrl_mask != KeyEvent.CTRL_MASK;
			if (e.getModifiers() == event_ctrl_mask
					&& e.getKeyCode() == KeyEvent.VK_A && isMac) {
				selectAll();
			} else if (e.getModifiers() == event_ctrl_mask
					&& e.getKeyCode() == KeyEvent.VK_X && isMac) {
				cut();
			} else if (e.getModifiers() == event_ctrl_mask
					&& e.getKeyCode() == KeyEvent.VK_C && isMac) {
				copy();
			} else if (e.getModifiers() == event_ctrl_mask
					&& e.getKeyCode() == KeyEvent.VK_V && isMac) {
				paste();
			} else if (e.getModifiers() == event_ctrl_mask
					&& e.getKeyCode() == KeyEvent.VK_Z) {
				if (undo.canUndo()) {
					undo.undo();
				}
			} else if (e.getModifiers() == event_ctrl_mask
					&& e.getKeyCode() == KeyEvent.VK_R) {
				if (undo.canRedo()) {
					undo.redo();
				}
			}
		}
	}
}