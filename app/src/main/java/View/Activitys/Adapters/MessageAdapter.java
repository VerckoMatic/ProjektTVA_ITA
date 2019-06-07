package View.Activitys.Adapters;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.matic.projekttva.R;

import java.util.ArrayList;
import java.util.List;

import Model.ResponsePOJO.MessagePOJO;

public class MessageAdapter extends BaseAdapter{
    List<MessagePOJO> messages = new ArrayList<MessagePOJO>();
    String uporabnikPrenos;
    Context context;

    public MessageAdapter(Context context) {
        this.context = context;
    }

    public void add(List<MessagePOJO> message,String uporabnik) {
        messages.clear();
        notifyDataSetChanged();
        messages.addAll(message);
        uporabnikPrenos = uporabnik;
        notifyDataSetChanged(); // to render the list we need to notify
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int i) {
        return messages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        MessageViewHolder holder = new MessageViewHolder();
        LayoutInflater messageInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        MessagePOJO message = messages.get(i);

        if (message.isBelongsToCurrentUser() && message.getEmail().equals(uporabnikPrenos)) {
            convertView = messageInflater.inflate(R.layout.my_message, null);
            holder.messageBody = (TextView) convertView.findViewById(R.id.message_body);
            convertView.setTag(holder);
            holder.messageBody.setText(message.getText());
        } else {
            convertView = messageInflater.inflate(R.layout.incomming_message, null);
            holder.avatar = (View) convertView.findViewById(R.id.avatar);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.messageBody = (TextView) convertView.findViewById(R.id.message_body);
            convertView.setTag(holder);

            holder.name.setText(message.getEmail());
            holder.messageBody.setText(message.getText());
        }

        return convertView;
    }

}

class MessageViewHolder {
    public View avatar;
    public TextView name;
    public TextView messageBody;
}