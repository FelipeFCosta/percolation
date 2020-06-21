package main.percolation;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;

import main.transitionframes.PercolatedFrame;

public class PercolationGUI {
    /**
     * The grid frame.
    */
    private final JFrame grid;
    
    /**
     * Object that runs the percolation system.
     */
    private final Percolation percolation;

    /**
     * Buttons that change colors according to its status in the percolation system.
     * Brown if the site (index) is open but not full.
     * Black if the site is blocked.
     * Blue if the site is full.
     */
    private final Buttons[] buttons;

     /**
     * List of indices of the buttons with open sites.
     */
    private final ArrayList<Integer> openBtns;

    /**
     * Used to set an N-by-N grid
     */
    private final int N;

    /**
     * Grid size + virtual top + virtual bottom
     */
    private final int length;

    /**
     * Has the system percolated yet?
     */
    private boolean percolated = false;

    /**
     * Used to set the default border for the last selected button by the keyboard arrows,
     * because the focus has changed.
     */
    private final Border defaultBorder;

    /**
     * Tracks the last selected button
     */
    private int lastFocusIdx = 0;

    private final Color blue = new Color(87, 145, 179);
    private final Color brown = new Color(120, 75, 60);
    private final Color gray = new Color(199, 249, 225);
    
    /**
     * @return {@code true} if the system has percolated
     */
    public boolean getPercolated() {
        return percolated;
    }

    /**
     * 
     * @param isPercolated
     * , to set the new binary status of the system
     */
    public void setPercolated(boolean isPercolated) {
        percolated = isPercolated;
    }

    /**
     * The gridLayout frame, a visualization of the percolation system.
     * @param N to set an N-by-N grid
     */
    public PercolationGUI(final int N) {
        grid = new JFrame();
        this.N = N;
        length = N * N + 2;
        buttons = new Buttons[length];
        percolation = new Percolation(N);
        openBtns = new ArrayList<>();

        final ActionListener buttonListener = new buttonActionListener();

        KeyListener keyListener = new keyActionListener();

        for (int i = 1; i < length - 1; i++) {
            buttons[i] = new Buttons(i);
            buttons[i].setBackground(Color.BLACK);
            buttons[i].addActionListener(buttonListener);
            buttons[i].addKeyListener(keyListener);
            grid.add(buttons[i]);
        }
        defaultBorder = buttons[1].getBorder();
        
        grid.setLayout(new GridLayout(N, N));
        grid.setTitle("Percolation");
        grid.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        grid.setResizable(false);
        grid.setSize(500 + N*5, 500 + N*5); // arbitrarily chosen formula
        grid.setLocationRelativeTo(null);
        grid.setVisible(true);
    }

    private void setSelectionBorder(JButton[] b, int idx) {
        b[idx].setBorder(new MatteBorder(2, 2, 2, 2, gray));
    }
    
    private class buttonActionListener implements ActionListener {
        @Override
        public void actionPerformed(final ActionEvent e) {
            final Buttons btn = ((Buttons) e.getSource());
            btn.setEnabled(true);
            btn.requestFocus();

            if(lastFocusIdx != 0) {
                buttons[lastFocusIdx].setBorder(defaultBorder);
            }

            if (!btn.isOpen()) {
                btn.setColor(brown);
                percolation.open(btn.getIdx());
                btn.setOpen(true);
                openBtns.add(btn.getIdx());
                for (int i = 0; i < openBtns.size(); i++) {
                    if (percolation.isFull(openBtns.get(i))) {
                        buttons[openBtns.get(i)].setColor(blue);
                    }
                }
                if (!getPercolated() && percolation.percolates()) {
                    setPercolated(true);
                    PercolatedFrame P = new PercolatedFrame(N, grid);
                    P.setVisible(true);
                    P.setLocationRelativeTo(grid);
                }  
            }
        }
    }

    private class keyActionListener extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            final Buttons btn = ((Buttons) e.getSource());
            int btnIdx = btn.getIdx();

            // enter key pressed
            if (e.getKeyCode() == KeyEvent.VK_ENTER && btn.getColor() == Color.BLACK) {
                ((JButton) e.getComponent()).doClick();
                return;
            }
            
            switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                if (btnIdx - N >= 1) {
                    buttons[btnIdx].setBorder(defaultBorder);
                    buttons[btnIdx - N].requestFocus();
                    lastFocusIdx = btnIdx - N;
                    setSelectionBorder(buttons, lastFocusIdx);
                }
                break;
            case KeyEvent.VK_DOWN:
                if (btnIdx + N <= length - 2) {
                    buttons[btnIdx].setBorder(defaultBorder);
                    buttons[btnIdx + N].requestFocus();
                    lastFocusIdx = btnIdx + N;
                    setSelectionBorder(buttons, lastFocusIdx);
                }
                break;
            case KeyEvent.VK_LEFT:
                if (btnIdx - 1 >= 1) {
                    buttons[btnIdx].setBorder(defaultBorder);
                    buttons[btnIdx - 1].requestFocus();
                    lastFocusIdx = btnIdx - 1;
                    setSelectionBorder(buttons, lastFocusIdx);
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (btnIdx + 1 <= N*N) {
                    buttons[btnIdx].setBorder(defaultBorder);
                    buttons[btnIdx + 1].requestFocus();
                    lastFocusIdx = btnIdx + 1;
                    setSelectionBorder(buttons, lastFocusIdx);
                }
                break;
            default:
                break;
            }
        }
    }

    @SuppressWarnings("serial")
    private class Buttons extends JButton {
        private boolean open;
        private Color color;
        private final int idx;

        public Buttons(int idx) {
            open = false;
            this.idx = idx;
            color = Color.BLACK;
        }

        public void setColor(Color color) {
            this.color = color;
            setBackground(color);
        }

        public Color getColor() {
            return color;
        }

        public int getIdx() {
            return idx;
        }

        public boolean isOpen() {
            return this.open;
        }

        public void setOpen(boolean open) {
            this.open = open;
        }
    }
}
