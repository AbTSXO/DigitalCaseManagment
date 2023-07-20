/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataEncoder;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 *
 * @author AbeXo
 */
public class TextFieldCharLimit extends PlainDocument {
    
    private int limit;
    
    public TextFieldCharLimit(int limitation)
    {
    this.limit=limitation;
    
    }
    
    public void insertString(int offset,String str,AttributeSet set)throws BadLocationException
    {
    if(str==null){return;}
    else if(getLength()+str.length()<=limit)
    {
        str=str.toUpperCase();
        super.insertString(offset,str,set);
    
    }
    
    
    }
    
}
