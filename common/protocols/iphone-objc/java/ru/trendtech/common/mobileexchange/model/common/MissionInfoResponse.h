
/**
 * Created by max on 13.02.14.
 */

@interface MissionInfoResponse : NSObject {
  MissionInfo * missionInfo;
}

@property(nonatomic, retain) MissionInfo * missionInfo;
- (void) setMissionInfo:(MissionInfo *)missionInfo;
@end
