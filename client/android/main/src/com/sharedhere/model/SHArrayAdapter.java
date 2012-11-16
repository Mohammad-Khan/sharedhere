package com.sharedhere.model;

//Getting the current file
import java.io.File;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharedhere.R;

public class SHArrayAdapter extends ArrayAdapter<File> {
	private final Context context;
	private final File[] files;

	public SHArrayAdapter(Context context, File[] files) {
		super(context, R.layout.upload, files);
		this.context = context;
		this.files = files;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View rowView = inflater.inflate(R.layout.upload, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.label);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.icon_file);
		
		textView.setText(files[position].getName());
		
		// Get the current file
		File f = files[position];
		
		// Select the right icon based on whether f is a folder or a regular
		if (f.isDirectory()) {
			imageView.setImageResource(R.drawable.folder);
		} else {
			imageView.setImageResource(R.drawable.file);
		}

		return rowView;
	}
}