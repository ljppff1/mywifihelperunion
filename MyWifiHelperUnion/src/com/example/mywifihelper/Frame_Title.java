package com.example.mywifihelper;


import com.webdesign688.shot360.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class Frame_Title extends Fragment implements OnClickListener
{  

	private String String_Title;
	private TextView mTv_Title;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
                             View layout = inflater.inflate(R.layout.frame_title, null);	
                             mTv_Title = (TextView) layout.findViewById(R.id.tv_frame_title);
                             mTv_Title.setText(String_Title);
                             layout.findViewById(R.id.imageView_home).setOnClickListener(this);
                             layout.findViewById(R.id.imageView_return).setOnClickListener(this);
		return layout ; 
	}

	public void setTitle(String string) {
              this.String_Title=string;		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageView_home:
			         startActivity(new Intent(getActivity(),MainActivity.class));
			         getActivity().finish();
			break;
       case R.id.imageView_return:
			        getActivity().finish();
			break;
		default:
			break;
		}
	}
	
}
