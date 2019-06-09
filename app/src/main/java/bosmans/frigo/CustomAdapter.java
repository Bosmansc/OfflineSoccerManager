package bosmans.frigo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {
    private Context context;


    public CustomAdapter(Context context) {

        this.context = context;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }
    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public int getCount() {
        return GoalsAndAssists.modelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return GoalsAndAssists.modelArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder(); LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_goals_and_assists_edit, null, true);


            holder.tvSpeler = (TextView) convertView.findViewById(R.id.speler);
            holder.tvnumber = (TextView) convertView.findViewById(R.id.number);
            holder.tvAssists = (TextView) convertView.findViewById(R.id.numberAssist);
            holder.btn_plus = (Button) convertView.findViewById(R.id.plus);
            holder.btn_plusAssists = convertView.findViewById(R.id.plusAssists);
            holder.btn_minus = (Button) convertView.findViewById(R.id.minus);
            holder.btn_minusAssists = convertView.findViewById(R.id.minusAssist);

            convertView.setTag(holder);
        }else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder)convertView.getTag();
        }

        holder.tvSpeler.setText(GoalsAndAssists.modelArrayList.get(position).getplayer());
        holder.tvnumber.setText(String.valueOf(GoalsAndAssists.modelArrayList.get(position).getNumber()));
        holder.tvAssists.setText(String.valueOf(GoalsAndAssists.modelArrayList.get(position).getAssists()));


        // plus Goals button
        holder.btn_plus.setTag(R.integer.btn_plus_view, convertView);
        holder.btn_plus.setTag(R.integer.btn_plus_pos, position);
        holder.btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View tempview = (View) holder.btn_plus.getTag(R.integer.btn_plus_view);
                TextView tv = (TextView) tempview.findViewById(R.id.number);
                Integer pos = (Integer) holder.btn_plus.getTag(R.integer.btn_plus_pos);

                int number = Integer.parseInt(tv.getText().toString()) + 1;
                tv.setText(String.valueOf(number));

                GoalsAndAssists.modelArrayList.get(pos).setNumber(number);

            }
        });

        // plus Assists button
        holder.btn_plusAssists.setTag(R.integer.btn_plusAssists_view, convertView);
        holder.btn_plusAssists.setTag(R.integer.btn_plusAssists_pos, position);
        holder.btn_plusAssists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View tempview = (View) holder.btn_plusAssists.getTag(R.integer.btn_plusAssists_view);
                TextView tvAssists = (TextView) tempview.findViewById(R.id.numberAssist);
                Integer posAssists = (Integer) holder.btn_plusAssists.getTag(R.integer.btn_plusAssists_pos);

                int numberAssists = Integer.parseInt(tvAssists.getText().toString()) + 1;
                tvAssists.setText(String.valueOf(numberAssists));

                GoalsAndAssists.modelArrayList.get(posAssists).setAssists(numberAssists);

            }
        });

        // minus Goals button
        holder.btn_minus.setTag(R.integer.btn_minus_view, convertView);
        holder.btn_minus.setTag(R.integer.btn_minus_pos, position);
        holder.btn_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View tempview = (View) holder.btn_minus.getTag(R.integer.btn_minus_view);
                TextView tv = (TextView) tempview.findViewById(R.id.number);
                Integer pos = (Integer) holder.btn_minus.getTag(R.integer.btn_minus_pos);

                int number = Integer.parseInt(tv.getText().toString()) - 1;
                tv.setText(String.valueOf(number));

                GoalsAndAssists.modelArrayList.get(pos).setNumber(number);

            }
        });

        // minus Assists button
        holder.btn_minusAssists.setTag(R.integer.btn_minusAssists_view, convertView);
        holder.btn_minusAssists.setTag(R.integer.btn_minusAssists_pos, position);
        holder.btn_minusAssists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View tempview = (View) holder.btn_minusAssists.getTag(R.integer.btn_minusAssists_view);
                TextView tvAssists = (TextView) tempview.findViewById(R.id.numberAssist);
                Integer posAssists = (Integer) holder.btn_minusAssists.getTag(R.integer.btn_minusAssists_pos);

                int numberAssists = Integer.parseInt(tvAssists.getText().toString()) - 1;
                tvAssists.setText(String.valueOf(numberAssists));

                GoalsAndAssists.modelArrayList.get(posAssists).setAssists(numberAssists);

            }
        });

        return convertView;
    }

    private class ViewHolder {

        protected Button btn_plus, btn_minus, btn_minusAssists, btn_plusAssists;
        private TextView tvSpeler, tvnumber, tvAssists;

    }
}
