#import "MissionInfo.h"

/**
 * Created by ivanenok on 4/14/2014.
 */

@interface DriverRequiredNotification : NSObject {
  MissionInfo * missionInfo;
}

@property(nonatomic, retain) MissionInfo * missionInfo;
- (void) setMissionInfo:(MissionInfo *)missionInfo;
@end
