package com.mauriciotogneri.shoppingcart.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.EditText;
import com.mauriciotogneri.shoppingcart.R;

public class CustomEditText extends EditText
{
	public CustomEditText(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init(context, attrs);
	}
	
	private void init(Context context, AttributeSet attrs)
	{
		TypedArray styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView);
		String fontName = styledAttributes.getString(R.styleable.CustomTextView_font);
		styledAttributes.recycle();
		
		if (!TextUtils.isEmpty(fontName))
		{
			Typeface font = Fonts.getFont(fontName);
			setTypeface(font);
		}
	}
	
	public String getTextValue()
	{
		return getText().toString();
	}
}