package cn.daixiaodong.myapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;

import java.util.List;

import cn.daixiaodong.myapp.R;
import cn.daixiaodong.myapp.view.NoUnderlineSpan;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by daixiaodong on 15/8/4.
 */
public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private OnCommentItemClickListener mListener;
    private List<AVObject> mDataSet;
    private LayoutInflater mInflater;
    private Context mContext;
    private NoUnderlineSpan mNoUnderlineSpan;

    public CommentAdapter(Context context, List<AVObject> dataSet) {
        this.mContext = context;
        this.mDataSet = dataSet;
        mInflater = LayoutInflater.from(context);
        mNoUnderlineSpan = new NoUnderlineSpan();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new CommentViewHolder(mInflater.inflate(R.layout.item_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        CommentViewHolder viewHolder = (CommentViewHolder) holder;

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                  //  mListener.onCommentItemClick(holder, position);
                }
            }
        });

        viewHolder.profilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onCommentItemProfilePhotoClick(holder, position);
                }
            }
        });

        viewHolder.username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onCommentItemUsernameClick(holder, position);
                }
            }
        });
        viewHolder.reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onCommentItemReplyClick(holder, position);
                }
            }
        });

        viewHolder.upvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onCommentItemUpvoteClick(holder, position);
                }
            }
        });

        viewHolder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onCommentItemMoreClick(holder, position);
                }
            }
        });


        AVObject object = mDataSet.get(position);
        AVUser user = object.getAVUser("user");
        AVUser replyToUser = object.getAVUser("replyToUser");
        viewHolder.username.setText(user.getUsername());
        StringBuilder content = new StringBuilder();
        viewHolder.content.setMovementMethod(LinkMovementMethod.getInstance());

        if (replyToUser != null) {
            content.append("回复 ");
            content.append("<a href=\"user_profile://daixiaodong.cn/?userId="+replyToUser.getObjectId()+"\">");
            content.append(replyToUser.getUsername());
            content.append("</a>");
            content.append(" ：");
            content.append(object.getString("content"));
            viewHolder.content.setText(Html.fromHtml(content.toString()));
            Spannable s = (Spannable) viewHolder.content.getText();
            s.setSpan(mNoUnderlineSpan, 3, replyToUser.getUsername().length() + 3, Spanned.SPAN_MARK_MARK);
        } else {
            viewHolder.content.setText(object.getString("content"));
        }


        //   viewHolder.content.setText(object.getString("content"));
       /* String url = "m://my.com/";
        String value = "";
        String text = String.format(mContext.getResources().getString(R.string.comment_username_2), value);
        Log.i("text", mContext.getResources().getString(R.string.comment_username));


        viewHolder.content.setMovementMethod(LinkMovementMethod.getInstance());
        viewHolder.content.setText(Html.fromHtml("<a href=\"m://my.com/\">打开app</a>" + "xxxxx" + "<a href=\"m://my.com/\">打开app</a>"));
//        viewHolder.content.setText(mContext.getResources().getString(R.string.comment_username));*/

    }


    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {

        CircleImageView profilePhoto;
        TextView username;
        TextView content;
        TextView reply;
        TextView upvote;
        TextView more;

        public CommentViewHolder(View itemView) {
            super(itemView);
            profilePhoto = (CircleImageView) itemView.findViewById(R.id.img_profile_photo);
            username = (TextView) itemView.findViewById(R.id.text_username);
            content = (TextView) itemView.findViewById(R.id.text_content);
            reply = (TextView) itemView.findViewById(R.id.text_reply);
            upvote = (TextView) itemView.findViewById(R.id.text_upvote);
            more = (TextView) itemView.findViewById(R.id.text_more);
        }
    }

    public interface OnCommentItemClickListener {
        void onCommentItemClick(RecyclerView.ViewHolder viewHolder, int position);

        void onCommentItemProfilePhotoClick(RecyclerView.ViewHolder viewHolder, int position);

        void onCommentItemUsernameClick(RecyclerView.ViewHolder viewHolder, int position);

        void onCommentItemReplyClick(RecyclerView.ViewHolder viewHolder, int position);
        void onCommentItemUpvoteClick(RecyclerView.ViewHolder viewHolder, int position);
        void onCommentItemMoreClick(RecyclerView.ViewHolder viewHolder, int position);


    }

    public void setOnCommentItemClickListener(OnCommentItemClickListener listener) {
        this.mListener = listener;
    }

}
