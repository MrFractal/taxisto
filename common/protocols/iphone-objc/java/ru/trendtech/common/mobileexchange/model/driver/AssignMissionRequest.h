#import "ItemLocation.h"

/**
 * Created by max on 06.02.14.
 */

@interface AssignMissionRequest : DriverRequest {
  long missionId;
  int arrivalTime;
  ItemLocation * location;
}

@property(nonatomic) long missionId;
@property(nonatomic) int arrivalTime;
@property(nonatomic, retain) ItemLocation * location;
- (void) setMissionId:(long)missionId;
- (void) setArrivalTime:(int)arrivalTime;
- (void) setLocation:(ItemLocation *)location;
@end
