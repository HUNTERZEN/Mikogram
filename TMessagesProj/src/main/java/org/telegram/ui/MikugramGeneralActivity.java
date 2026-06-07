package org.telegram.ui;

import static org.telegram.messenger.LocaleController.getString;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.telegram.messenger.MikugramConfig;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;

public class MikugramGeneralActivity extends BaseFragment {

    private RecyclerListView listView;
    private ListAdapter listAdapter;
    private int rowCount;

    private int headerRow;
    private int useSystemEmojiRow;
    private int disableNumberRoundingRow;
    private int askBeforeCallRow;
    private int openArchiveOnPullRow;
    private int preferIPv6Row;
    private int infoRow;

    @Override
    public boolean onFragmentCreate() {
        rowCount = 0;
        headerRow = rowCount++;
        useSystemEmojiRow = rowCount++;
        disableNumberRoundingRow = rowCount++;
        askBeforeCallRow = rowCount++;
        openArchiveOnPullRow = rowCount++;
        preferIPv6Row = rowCount++;
        infoRow = rowCount++;
        return super.onFragmentCreate();
    }

    @Override
    public View createView(Context context) {
        actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        actionBar.setAllowOverlayTitle(true);
        actionBar.setTitle(getString(R.string.MikugramGeneral));
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int id) {
                if (id == -1) finishFragment();
            }
        });

        listAdapter = new ListAdapter(context);
        FrameLayout frameLayout = new FrameLayout(context);
        frameLayout.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
        fragmentView = frameLayout;

        listView = new RecyclerListView(context);
        listView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        listView.setVerticalScrollBarEnabled(false);
        listView.setAdapter(listAdapter);
        frameLayout.addView(listView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        listView.setOnItemClickListener((view, position) -> {
            if (position == useSystemEmojiRow) {
                MikugramConfig.toggleUseSystemEmoji();
                if (view instanceof TextCheckCell) ((TextCheckCell) view).setChecked(MikugramConfig.isUseSystemEmoji());
            } else if (position == disableNumberRoundingRow) {
                MikugramConfig.toggleDisableNumberRounding();
                if (view instanceof TextCheckCell) ((TextCheckCell) view).setChecked(MikugramConfig.isDisableNumberRounding());
            } else if (position == askBeforeCallRow) {
                MikugramConfig.toggleAskBeforeCall();
                if (view instanceof TextCheckCell) ((TextCheckCell) view).setChecked(MikugramConfig.isAskBeforeCall());
            } else if (position == openArchiveOnPullRow) {
                MikugramConfig.toggleOpenArchiveOnPull();
                if (view instanceof TextCheckCell) ((TextCheckCell) view).setChecked(MikugramConfig.isOpenArchiveOnPull());
            } else if (position == preferIPv6Row) {
                MikugramConfig.togglePreferIPv6();
                if (view instanceof TextCheckCell) ((TextCheckCell) view).setChecked(MikugramConfig.isPreferIPv6());
            }
        });

        return fragmentView;
    }

    private class ListAdapter extends RecyclerListView.SelectionAdapter {
        private Context mContext;
        public ListAdapter(Context context) { mContext = context; }

        @Override public boolean isEnabled(RecyclerView.ViewHolder holder) {
            return holder.getItemViewType() == 1;
        }
        @Override public int getItemCount() { return rowCount; }

        @NonNull @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;
            switch (viewType) {
                case 0: view = new HeaderCell(mContext); view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite)); break;
                case 1: view = new TextCheckCell(mContext); view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite)); break;
                default: view = new TextInfoPrivacyCell(mContext); break;
            }
            return new RecyclerListView.Holder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            switch (holder.getItemViewType()) {
                case 0:
                    ((HeaderCell) holder.itemView).setText(getString(R.string.MikugramGeneral));
                    break;
                case 1: {
                    TextCheckCell cell = (TextCheckCell) holder.itemView;
                    if (position == useSystemEmojiRow) cell.setTextAndCheck(getString(R.string.MikugramUseSystemEmoji), MikugramConfig.isUseSystemEmoji(), true);
                    else if (position == disableNumberRoundingRow) cell.setTextAndCheck(getString(R.string.MikugramDisableNumberRounding), MikugramConfig.isDisableNumberRounding(), true);
                    else if (position == askBeforeCallRow) cell.setTextAndCheck(getString(R.string.MikugramAskBeforeCall), MikugramConfig.isAskBeforeCall(), true);
                    else if (position == openArchiveOnPullRow) cell.setTextAndCheck(getString(R.string.MikugramOpenArchiveOnPull), MikugramConfig.isOpenArchiveOnPull(), true);
                    else if (position == preferIPv6Row) cell.setTextAndCheck(getString(R.string.MikugramPreferIPv6), MikugramConfig.isPreferIPv6(), false);
                    break;
                }
                case 2:
                    ((TextInfoPrivacyCell) holder.itemView).setText(getString(R.string.MikugramPreferIPv6Info));
                    break;
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position == headerRow) return 0;
            if (position == infoRow) return 2;
            return 1;
        }
    }
}
