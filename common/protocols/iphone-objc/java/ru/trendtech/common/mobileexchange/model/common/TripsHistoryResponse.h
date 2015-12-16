#import "NSMutableArray.h"
#import "NSMutableArray.h"

/**
 * Created by max on 06.02.14.
 */

@interface TripsHistoryResponse : NSObject {
  NSMutableArray * history;
}

@property(nonatomic, retain, readonly) NSMutableArray * history;
- (void) init;
@end
