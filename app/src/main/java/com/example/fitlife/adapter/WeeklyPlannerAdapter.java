package com.example.fitlife.adapter;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitlife.R;
import com.example.fitlife.data.model.WeeklyPlan;
import com.example.fitlife.data.model.WorkoutRoutine;
import com.example.fitlife.utils.DayDisplayInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WeeklyPlannerAdapter extends RecyclerView.Adapter<WeeklyPlannerAdapter.DayViewHolder> {

    private List<DayDisplayInfo> weekDays = new ArrayList<>();
    private Map<Integer, WeeklyPlan> weeklyPlans;
    private Map<Long, WorkoutRoutine> routines;
    private OnDayClickListener listener;

    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_day_card, parent, false);
        return new DayViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, int position) {
        DayDisplayInfo currentDay = weekDays.get(position);
        holder.dayName.setText(currentDay.dayName);
        holder.dayDate.setText(currentDay.date);

        // Highlight today's date
        if (currentDay.isToday) {
            holder.dayName.setTypeface(null, Typeface.BOLD);
            holder.dayDate.setTypeface(null, Typeface.BOLD);
            holder.dayName.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.primary));
        } else {
            holder.dayName.setTypeface(null, Typeface.NORMAL);
            holder.dayDate.setTypeface(null, Typeface.NORMAL);
            holder.dayName.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.black));
        }

        WeeklyPlan plan = weeklyPlans != null ? weeklyPlans.get(position + 1) : null;

        if (plan != null && routines != null) {
            WorkoutRoutine routine = routines.get(plan.getRoutineId());
            if (routine != null) {
                holder.assignedRoutineName.setText(routine.getName());
            } else {
                 holder.assignedRoutineName.setText("Tap to assign routine");
            }
            holder.dayCompletedCheckbox.setChecked(plan.isCompleted());
        } else {
            holder.assignedRoutineName.setText("Tap to assign routine");
            holder.dayCompletedCheckbox.setChecked(false);
        }

        holder.dayCompletedCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (listener != null && plan != null) {
                listener.onDayCompletionChanged(plan, isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return weekDays.size();
    }

    public void setWeekDays(List<DayDisplayInfo> weekDays) {
        this.weekDays = weekDays;
        notifyDataSetChanged();
    }

    public void setWeeklyData(Map<Integer, WeeklyPlan> weeklyPlans, Map<Long, WorkoutRoutine> routines) {
        this.weeklyPlans = weeklyPlans;
        this.routines = routines;
        notifyDataSetChanged();
    }

    class DayViewHolder extends RecyclerView.ViewHolder {
        private final TextView dayName;
        private final TextView dayDate;
        private final TextView assignedRoutineName;
        private final CheckBox dayCompletedCheckbox;

        public DayViewHolder(@NonNull View itemView) {
            super(itemView);
            dayName = itemView.findViewById(R.id.day_name);
            dayDate = itemView.findViewById(R.id.day_date);
            assignedRoutineName = itemView.findViewById(R.id.assigned_routine_name);
            dayCompletedCheckbox = itemView.findViewById(R.id.day_completed_checkbox);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onDayClicked(position + 1); // Day of week is 1-based
                }
            });
        }
    }

    public interface OnDayClickListener {
        void onDayClicked(int dayOfWeek);
        void onDayCompletionChanged(WeeklyPlan plan, boolean isCompleted);
    }

    public void setOnDayClickListener(OnDayClickListener listener) {
        this.listener = listener;
    }
}
