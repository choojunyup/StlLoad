package com.example.administrator.stlload;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class ListViewAdapter extends BaseAdapter {

	private Context mContext = null;
	private ArrayList<FileInfo> mListFile = new ArrayList<FileInfo>();
	
	public ListViewAdapter(Context context) {
		super();
		this.mContext = context;
	}

	public void addItem(FileInfo fileInfo){
		mListFile.add(fileInfo);
	}
	
	public void remove(int position){
		mListFile.remove(position);
	}
	
	public void sort(){
		Collections.sort(mListFile, FileInfo.ALPHA_COMPARATOR);
	}
	
	@Override
	public int getCount() {
		return mListFile.size();
	}

	@Override
	public Object getItem(int position) {
		return mListFile.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();
		
			LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.fileinfo, null);
		
			holder.ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
	        holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
	        holder.tvDate = (TextView) convertView.findViewById(R.id.tvDate);
	        holder.tvSize = (TextView) convertView.findViewById(R.id.tvSize);
	 
	        convertView.setTag(holder);
	    }else{
	    	holder = (ViewHolder) convertView.getTag();
	    }
	 
	    FileInfo mFile = mListFile.get(position);
	 
	    if (mFile.getFileIcon() != 0) {
	        holder.ivImage.setVisibility(View.VISIBLE);
	        holder.ivImage.setImageResource(mFile.getFileIcon());
	    }else{
	        holder.ivImage.setVisibility(View.GONE);
	    }
	 
	    holder.tvName.setText(mFile.getFileName());
	    holder.tvDate.setText(mFile.getFileDate());
	    if(mFile.getFileSize()!=null && !mFile.getFileSize().equals("")){
		    String size = (Long.valueOf(mFile.getFileSize()) / 1024) + "KByte";
		    holder.tvSize.setText(size);
	    }else{
	    	holder.tvSize.setText("");
	    }
	    /*
	    if(FileUtil.isImage(mFile.getFilePath())){
	    	Bitmap bm = BitmapFactory.decodeFile(mFile.getFilePath());
	    	holder.ivImage.setVisibility(View.VISIBLE);
	    	holder.ivImage.setImageBitmap(bm);
	    }
	    */
	    return convertView;
		
	}
	
	class ViewHolder {
		public ImageView ivImage;
		public TextView tvName;
		public TextView tvDate;
		public TextView tvSize;
	}
	
}
