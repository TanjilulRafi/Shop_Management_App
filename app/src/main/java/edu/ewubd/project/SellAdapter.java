package edu.ewubd.project;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import java.util.ArrayList;

public class SellAdapter extends ArrayAdapter<ItemSell> {

    private final Context context;
    private final ArrayList<ItemSell> values;


    public SellAdapter(@NonNull Context context, @NonNull ArrayList<ItemSell> objects) {
        super(context, -1, objects);
        this.context = context;
        this.values = objects;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.list_item_sell, parent, false);
        TextView Name = rowView.findViewById(R.id.sitName);
        TextView Quantity = rowView.findViewById(R.id.sitQuantity);
        TextView Uprice = rowView.findViewById(R.id.sitUprice);
        TextView Subtotal = rowView.findViewById(R.id.subtotal);
        TextView Time = rowView.findViewById(R.id.timeer);
        Name.setText(values.get(position).iname);
        Quantity.setText(values.get(position).iqunat);
        Uprice.setText(values.get(position).iup);
        Subtotal.setText(values.get(position).st);
        Time.setText(values.get(position).time);
        return rowView;
    }
}