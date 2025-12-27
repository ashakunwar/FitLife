package com.example.fitlife.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fitlife.R;
import com.example.fitlife.data.model.GymLocation;
import com.example.fitlife.data.model.WorkoutRoutine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoutineAdapter extends RecyclerView.Adapter<RoutineAdapter.RoutineViewHolder> {

    private List<WorkoutRoutine> routines = new ArrayList<>();
    private Map<Long, GymLocation> locationsMap = new HashMap<>();
    private OnRoutineListener listener;
    private Context context;

    public RoutineAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RoutineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_routine_card, parent, false);
        return new RoutineViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RoutineViewHolder holder, int position) {
        WorkoutRoutine currentRoutine = routines.get(position);
        holder.nameTextView.setText(currentRoutine.getName());
        holder.exercisesTextView.setText(currentRoutine.getExercises());
        holder.equipmentTextView.setText(currentRoutine.getEquipment());

        // Image Loading
        if (currentRoutine.getImageUri() != null && !currentRoutine.getImageUri().isEmpty()) {
            Glide.with(context).load(Uri.parse(currentRoutine.getImageUri())).into(holder.routineImageView);
        } else {
            holder.routineImageView.setImageResource(R.drawable.ic_fitness); // Placeholder
        }

        // Location Handling
        if (currentRoutine.getLocationId() > 0 && locationsMap != null && locationsMap.containsKey(currentRoutine.getLocationId())) {
            GymLocation location = locationsMap.get(currentRoutine.getLocationId());
            holder.locationNameTextView.setText(location.getName());
            holder.locationNameTextView.setVisibility(View.VISIBLE);
            holder.navigateButton.setVisibility(View.VISIBLE);

            holder.navigateButton.setOnClickListener(v -> {
                String uri = "geo:" + location.getLatitude() + "," + location.getLongitude() + "?q=" + location.getLatitude() + "," + location.getLongitude() + "(" + location.getName() + ")";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                context.startActivity(intent);
            });
        } else {
            holder.locationNameTextView.setVisibility(View.GONE);
            holder.navigateButton.setVisibility(View.GONE);
        }

        // Checkbox state
        holder.completedCheckBox.setOnCheckedChangeListener(null);
        holder.completedCheckBox.setChecked(currentRoutine.isCompleted());
        holder.completedCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (listener != null) {
                listener.onRoutineCompletionChanged(currentRoutine, isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return routines.size();
    }

    public void setRoutines(List<WorkoutRoutine> routines) {
        this.routines = routines;
        notifyDataSetChanged();
    }

    public void setLocations(List<GymLocation> locations) {
        this.locationsMap.clear();
        for (GymLocation loc : locations) {
            this.locationsMap.put(loc.getId(), loc);
        }
        notifyDataSetChanged(); // Re-render to show location names
    }

    class RoutineViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameTextView;
        private final TextView exercisesTextView;
        private final TextView equipmentTextView;
        private final ImageView routineImageView;
        private final CheckBox completedCheckBox;
        private final ImageButton optionsMenuButton;
        private final TextView locationNameTextView;
        private final Button navigateButton;

        public RoutineViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.routine_name);
            exercisesTextView = itemView.findViewById(R.id.exercises_summary);
            equipmentTextView = itemView.findViewById(R.id.equipment_summary);
            routineImageView = itemView.findViewById(R.id.routine_image);
            completedCheckBox = itemView.findViewById(R.id.completed_checkbox);
            optionsMenuButton = itemView.findViewById(R.id.options_menu);
            locationNameTextView = itemView.findViewById(R.id.location_name);
            navigateButton = itemView.findViewById(R.id.navigate_button);

            optionsMenuButton.setOnClickListener(this::showPopupMenu);
        }

        private void showPopupMenu(View view) {
            PopupMenu popup = new PopupMenu(view.getContext(), view);
            popup.inflate(R.menu.menu_routine_options);
            popup.setOnMenuItemClickListener(item -> {
                int position = getAdapterPosition();
                if (position == RecyclerView.NO_POSITION) return false;

                WorkoutRoutine routine = routines.get(position);
                int itemId = item.getItemId();
                if (itemId == R.id.action_edit) {
                    listener.onEditClicked(routine);
                    return true;
                } else if (itemId == R.id.action_delete) {
                    listener.onDeleteClicked(routine);
                    return true;
                } else if (itemId == R.id.action_share_sms) {
                    listener.onShareSmsClicked(routine);
                    return true;
                }
                return false;
            });
            popup.show();
        }
    }

    public interface OnRoutineListener {
        void onEditClicked(WorkoutRoutine routine);
        void onDeleteClicked(WorkoutRoutine routine);
        void onShareSmsClicked(WorkoutRoutine routine);
        void onRoutineCompletionChanged(WorkoutRoutine routine, boolean isCompleted);
    }

    public void setOnRoutineListener(OnRoutineListener listener) {
        this.listener = listener;
    }
}
