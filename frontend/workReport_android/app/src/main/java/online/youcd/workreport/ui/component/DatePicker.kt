package online.youcd.workreport.ui.component

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import java.util.Calendar
import java.util.Date


/**
 * 日期选择器
 * @param [isShow] 是否显示
 * @param [onShow] 显示事件
 * @param [onSelect] 选择日期事件
 */
@Composable
fun NewDatePicker(
    isShow: Boolean,
    onShow: () -> Unit,
    onSelect: (String) -> Unit
) {
    val mContext = LocalContext.current

    val mYear: Int
    val mMonth: Int
    val mDay: Int

    val mCalendar = Calendar.getInstance()

    mYear = mCalendar.get(Calendar.YEAR)
    mMonth = mCalendar.get(Calendar.MONTH)
    mDay = mCalendar.get(Calendar.DAY_OF_MONTH)

    mCalendar.time = Date()

    val mDatePickerDialog = DatePickerDialog(
        mContext,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            onSelect("$mYear/${mMonth + 1}/$mDayOfMonth")
        },
        mYear,
        mMonth,
        mDay
    )
    //  打开 DatePicker
    if (isShow) {
        mDatePickerDialog.show()
        onShow()
    }
}
