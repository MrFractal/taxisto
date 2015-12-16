
/**
 * Created by max on 09.02.14.
 */

@interface DeclineMissionResponse : NSObject {
  long missionId;
}

@property(nonatomic) long missionId;
- (void) setMissionId:(long)missionId;
@end
