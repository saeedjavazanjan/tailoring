package com.saeeed.devejump.project.tailoring.utils

object TimeConvertor  {

    fun timeAndTimeUnitCalculator(date:Long):String{
        val currentTime=System.currentTimeMillis()
        var timeUnit="ثانیه"
        val diffrence=(currentTime-date)/1000
        var dif=diffrence

        when(diffrence){
            in 61..3599-> {
                timeUnit="دقیقه"
                dif=diffrence/60
            }
            in 3600..215999->{
                timeUnit="ساعت"
                dif=diffrence/3600

            }
            in 216000..5183999->{
                timeUnit="روز"
                dif=diffrence/216000
            }
            in 5184000..155519999->{
                timeUnit="ماه"
                dif=diffrence/5184000

            }
            in 155520000..Long.MAX_VALUE->{
                timeUnit="سال"
                dif=diffrence/155520000

            }
            else-> timeUnit="-"
        }
        return "$dif $timeUnit پیش "

    }

}