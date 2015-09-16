package com.example.ab.news.adapters;

import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.commons.lang3.StringEscapeUtils;

import com.example.ab.news.R;
import com.example.ab.news.data.Comment;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by ab on 2/18/15.
 */
public class CommentAdapter extends ArrayAdapter<Comment> {

    public CommentAdapter(Context context, List<Comment> comments) {
        super(context, 0, comments);
    }

    /**
     * {@inheritDoc}
     *
     * @param position
     * @param convertView
     * @param parent
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.article_comment_row, null);
        }

        final Comment comment = getItem(position);
        TextView titleView;

        titleView = (TextView) view.findViewById(R.id.articleComment);
        LinearLayout.LayoutParams llp0 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        llp0.setMargins(comment.getLevel()*30, 0, 0, 0); // llp.setMargins(left, top, right, bottom);

        titleView.setLayoutParams(llp0);
        titleView.setText(Html.fromHtml(StringEscapeUtils.unescapeHtml4(comment.getText())));
        titleView.setMovementMethod(LinkMovementMethod.getInstance());

        return view;
    }

}
