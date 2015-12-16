
/**
 * Created by max on 06.02.14.
 */

@interface AssignMissionResponse : NSObject {
  BOOL assigned;
}

@property(nonatomic) BOOL assigned;
- (void) init;
- (void) setAssigned:(BOOL)assigned;
@end
