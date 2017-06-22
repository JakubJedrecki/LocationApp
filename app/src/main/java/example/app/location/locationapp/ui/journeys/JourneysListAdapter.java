package example.app.location.locationapp.ui.journeys;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import example.app.location.locationapp.R;
import example.app.location.locationapp.models.Journey;

/**
 * Created by Kuba on 18.06.2017.
 */

public class JourneysListAdapter extends RecyclerView.Adapter<JourneysListAdapter.ViewHolder> {

    private List<Journey> journeys;
    private ClickListener clickListener;
    private Context context;

    public JourneysListAdapter(List<Journey> journeys, ClickListener clickListener, Context context) {
        this.journeys = journeys;
        this.clickListener = clickListener;
        this.context = context;
    }

    @Override
    public JourneysListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.journey_card_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(JourneysListAdapter.ViewHolder holder, int position) {
        if(journeys != null){
            holder.journeyName.setText(String.format(Locale.getDefault(), "%1$s %2$d", context.getResources().getString(R.string.journey), journeys.get(position).getId()));
            DateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
            holder.journeyDate.setText(simpleDateFormat.format(new Date(journeys.get(position).getTimeStart())));
        }
    }

    @Override
    public int getItemCount() {
        return journeys.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.journeyName) TextView journeyName;
        @BindView(R.id.journeyDate) TextView journeyDate;

        public ViewHolder(View view){
            super(view);
            ButterKnife.bind(this, view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(clickListener != null){
                clickListener.selectedJourney(view, getAdapterPosition());
            }
        }
    }

    public interface ClickListener{
        void selectedJourney(View view, int position);
    }
}
