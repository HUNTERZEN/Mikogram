package org.telegram.ui;

import static org.telegram.messenger.AndroidUtilities.dp;
import static org.telegram.messenger.LocaleController.getString;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;

public class MikugramPreferencesActivity extends BaseFragment {

    private RecyclerListView listView;
    private ListAdapter listAdapter;

    private int rowCount;

    // Header
    private int headerRow;
    private int headerShadowRow;

    // Categories
    private int generalRow;
    private int appearanceRow;
    private int chatRow;
    private int experimentalRow;
    private int categoriesShadowRow;

    // About
    private int aboutHeaderRow;
    private int versionRow;
    private int sourceCodeRow;
    private int aboutShadowRow;

    @Override
    public boolean onFragmentCreate() {
        updateRows();
        return super.onFragmentCreate();
    }

    private void updateRows() {
        rowCount = 0;

        headerRow = rowCount++;
        headerShadowRow = rowCount++;

        generalRow = rowCount++;
        appearanceRow = rowCount++;
        chatRow = rowCount++;
        experimentalRow = rowCount++;
        categoriesShadowRow = rowCount++;

        aboutHeaderRow = rowCount++;
        versionRow = rowCount++;
        sourceCodeRow = rowCount++;
        aboutShadowRow = rowCount++;
    }

    @Override
    public View createView(Context context) {
        actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        actionBar.setAllowOverlayTitle(true);
        actionBar.setTitle(getString(R.string.MikugramPreferences));
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int id) {
                if (id == -1) {
                    finishFragment();
                }
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
            if (position == generalRow) {
                presentFragment(new MikugramGeneralActivity());
            } else if (position == appearanceRow) {
                presentFragment(new MikugramAppearanceActivity());
            } else if (position == chatRow) {
                presentFragment(new MikugramChatActivity());
            } else if (position == experimentalRow) {
                presentFragment(new MikugramExperimentalActivity());
            } else if (position == sourceCodeRow) {
                org.telegram.messenger.browser.Browser.openUrl(getParentActivity(), "https://github.com/HUNTERZEN/Mikogram.git");
            }
        });

        return fragmentView;
    }

    private String getVersionString() {
        try {
            PackageInfo pInfo = ApplicationLoader.applicationContext.getPackageManager()
                    .getPackageInfo(ApplicationLoader.applicationContext.getPackageName(), 0);
            return pInfo.versionName + " (" + pInfo.versionCode + ")";
        } catch (Exception e) {
            return "Unknown";
        }
    }

    private class ListAdapter extends RecyclerListView.SelectionAdapter {

        private Context mContext;

        private static final int VIEW_TYPE_HEADER_VIEW = 0;
        private static final int VIEW_TYPE_TEXT_CELL = 1;
        private static final int VIEW_TYPE_SHADOW = 2;
        private static final int VIEW_TYPE_HEADER = 3;
        private static final int VIEW_TYPE_TEXT_SETTINGS = 4;

        public ListAdapter(Context context) {
            mContext = context;
        }

        @Override
        public boolean isEnabled(RecyclerView.ViewHolder holder) {
            int type = holder.getItemViewType();
            return type == VIEW_TYPE_TEXT_CELL || type == VIEW_TYPE_TEXT_SETTINGS;
        }

        @Override
        public int getItemCount() {
            return rowCount;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;
            switch (viewType) {
                case VIEW_TYPE_HEADER_VIEW: {
                    // Create the Nekogram-style header with icon + name + version, stretched to match parent width
                    LinearLayout headerLayout = new LinearLayout(mContext);
                    headerLayout.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
                    headerLayout.setOrientation(LinearLayout.VERTICAL);
                    headerLayout.setGravity(Gravity.CENTER_HORIZONTAL);
                    headerLayout.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    headerLayout.setPadding(0, dp(32), 0, dp(24));

                    // App icon
                    ImageView iconView = new ImageView(mContext);
                    iconView.setImageResource(R.mipmap.ic_launcher);
                    iconView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    headerLayout.addView(iconView, LayoutHelper.createLinear(90, 90, Gravity.CENTER_HORIZONTAL, 0, 0, 0, 12));

                    // App name
                    TextView nameView = new TextView(mContext);
                    nameView.setText("Mikogram");
                    nameView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 22);
                    nameView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
                    nameView.setTypeface(AndroidUtilities.bold());
                    nameView.setGravity(Gravity.CENTER);
                    headerLayout.addView(nameView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL, 0, 0, 0, 4));

                    // Version
                    TextView versionView = new TextView(mContext);
                    versionView.setText(getVersionString());
                    versionView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
                    versionView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText2));
                    versionView.setGravity(Gravity.CENTER);
                    headerLayout.addView(versionView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL));

                    view = headerLayout;
                    break;
                }
                case VIEW_TYPE_TEXT_CELL: {
                    view = new TextCell(mContext);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                }
                case VIEW_TYPE_SHADOW: {
                    view = new ShadowSectionCell(mContext);
                    break;
                }
                case VIEW_TYPE_HEADER: {
                    view = new HeaderCell(mContext);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                }
                case VIEW_TYPE_TEXT_SETTINGS:
                default: {
                    view = new TextCell(mContext);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                }
            }
            return new RecyclerListView.Holder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            switch (holder.getItemViewType()) {
                case VIEW_TYPE_HEADER_VIEW:
                    // Already built in onCreateViewHolder
                    break;
                case VIEW_TYPE_TEXT_CELL: {
                    TextCell cell = (TextCell) holder.itemView;
                    if (position == generalRow) {
                        cell.setTextAndIcon(getString(R.string.MikugramGeneral), R.drawable.msg_media, true);
                    } else if (position == appearanceRow) {
                        cell.setTextAndIcon(getString(R.string.MikugramAppearance), R.drawable.msg_palette, true);
                    } else if (position == chatRow) {
                        cell.setTextAndIcon(getString(R.string.MikugramChats), R.drawable.msg_discussion, true);
                    } else if (position == experimentalRow) {
                        cell.setTextAndIcon(getString(R.string.MikugramExperimental), R.drawable.msg_fave, false);
                    }
                    break;
                }
                case VIEW_TYPE_TEXT_SETTINGS: {
                    TextCell cell = (TextCell) holder.itemView;
                    if (position == versionRow) {
                        cell.setTextAndValueAndIcon(getString(R.string.MikugramVersion), getVersionString(), R.drawable.msg_info, true);
                    } else if (position == sourceCodeRow) {
                        cell.setTextAndValueAndIcon(getString(R.string.MikugramSourceCode), getString(R.string.MikugramSourceCodeValue), R.drawable.msg_link, false);
                    }
                    break;
                }
                case VIEW_TYPE_HEADER: {
                    HeaderCell cell = (HeaderCell) holder.itemView;
                    if (position == aboutHeaderRow) {
                        cell.setText(getString(R.string.MikugramAbout));
                    }
                    break;
                }
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position == headerRow) {
                return VIEW_TYPE_HEADER_VIEW;
            } else if (position == generalRow || position == appearanceRow ||
                       position == chatRow || position == experimentalRow) {
                return VIEW_TYPE_TEXT_CELL;
            } else if (position == headerShadowRow || position == categoriesShadowRow || position == aboutShadowRow) {
                return VIEW_TYPE_SHADOW;
            } else if (position == aboutHeaderRow) {
                return VIEW_TYPE_HEADER;
            } else if (position == versionRow || position == sourceCodeRow) {
                return VIEW_TYPE_TEXT_SETTINGS;
            }
            return VIEW_TYPE_SHADOW;
        }
    }
}
