#import "ItemLocation.h"

/**
 * Created by max on 13.02.14.
 */

@interface TripPauseRequest : DriverRequest {
  long missionId;
  ItemLocation * location;
  BOOL pauseBegin;
  int pauseTime;
}

@property(nonatomic) long missionId;
@property(nonatomic, retain) ItemLocation * location;
@property(nonatomic) BOOL pauseBegin;
@property(nonatomic) int pauseTime;
- (void) setMissionId:(long)missionId;
- (void) setLocation:(ItemLocation *)location;
- (void) setPauseBegin:(BOOL)pauseBegin;
- (void) setPauseTime:(int)pauseTime;
@end
