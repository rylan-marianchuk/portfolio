// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.Arrays;

public final class FindMeetingQuery {
    public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
        /*
            params:
                MeetingRequest request: specifies (1) a duration of time and (2) a collection of attendees
                Collection<Event> events: specifies the times during day which users are busy

            RETURN: A collection of intervals (time ranges) which are valid solutions to the meeting request. Valid 
            meaning each interval is at least the specified duration and does not overlap with any events.
        */

        // Brief Approach: Add intervals starting from time 0 going forward through the sorted events by start time.

        ArrayList<TimeRange> validIntervals = new ArrayList<TimeRange>();


        /*          Edge Cases            */

        // Request duration must be less than or equal to a day
        if (request.getDuration() > TimeRange.WHOLE_DAY.duration()){
            // Return an empty collection of valid intervals since non should exist
            return validIntervals;
        }

        if (request.getAttendees().isEmpty() || events.isEmpty()){
            // The whole day could then be scheduled
            validIntervals.add(TimeRange.WHOLE_DAY);
            return validIntervals;
        }

        // Sort the events by increasing start time
        ArrayList<Event> sortedEvents = new ArrayList<>(events);      

        Collections.sort(sortedEvents, new Comparator<Event>() {
            @Override
            public int compare(Event a, Event b) {
                return Long.compare(a.getWhen().start(), b.getWhen().start());
            }
        });
        
        
        // Greedily loop and take away intervals, capturing an end of an event with vacancy thereafter

        int curr_start = TimeRange.START_OF_DAY;
        while (!sortedEvents.isEmpty()){
            Event leastMost = sortedEvents.get(0);

            // Void this event if it does not include members from the meeting request
            if (!includesRequestMembers(leastMost, request)) {
                sortedEvents.remove(0);
                continue;
            }

            if (leastMost.getWhen().start() > curr_start){
                // No Overlap, check if this gap is sufficient length

                TimeRange potentiallyValid = TimeRange.fromStartEnd(curr_start, leastMost.getWhen().start(), false);
                if (potentiallyValid.duration() >= request.getDuration()){
                    // curr_start time to start of leastMost is great enough
                    validIntervals.add(potentiallyValid);
                }
                
                curr_start = leastMost.getWhen().end();
            }
            else  if (leastMost.getWhen().end() > curr_start){
                // Overlap
                curr_start = leastMost.getWhen().end();
            }
            // Else this interval is fully contained, simply remove it 

            sortedEvents.remove(0);
        }

        // Consider the final time interval
        if (TimeRange.fromStartEnd(curr_start, TimeRange.END_OF_DAY, true).duration() > request.getDuration()){
            // This final start time to end of day is long enough
            validIntervals.add(TimeRange.fromStartEnd(curr_start, TimeRange.END_OF_DAY, true));
        }

        return validIntervals;
    }


    private boolean includesRequestMembers(Event e, MeetingRequest request){
        // Return true if this event has (existence) of any member in request, false otherwise
        for (String attender : e.getAttendees()){
            if (request.getAttendees().contains(attender)){
                return true;
            }
        } 
        return false;
    }
}
